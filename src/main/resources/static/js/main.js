const apiUrl = '/api';

// Получить всех пользователей и отобразить в таблице
async function getAllUsers() {
    const response = await fetch(`${apiUrl}/users`);
    const users = await response.json();
    displayUsers(users);
}

// Получить все роли (для форм)
async function getAllRoles() {
    const response = await fetch(`${apiUrl}/roles`);
    return await response.json();
}

// Получить текущего пользователя (user.html)
async function getAuthUser() {
    // Получаем email из DOM (шапка страницы)
    const email = document.querySelector('.text-white.font-weight-bold')?.textContent?.trim();
    if (!email) return;
    const response = await fetch(`${apiUrl}/users`);
    const users = await response.json();
    const user = users.find(u => u.email === email);
    if (user) displayAuthUser(user);
}


// Добавить пользователя
async function createUser(userData) {
    const response = await fetch(`${apiUrl}/users`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userData)
    });
    if (!response.ok) {
        const errors = await response.json();
        showValidationErrors(errors);
        return;
    }
    await getAllUsers();
}

// Функция для отображения ошибок валидации
function showValidationErrors(errors) {
    let msg = errors.map(e => e.defaultMessage).join('\n');
    alert('Ошибка валидации:\n' + msg);
}

// Обновить пользователя
async function updateUser(id, userData) {
    await fetch(`${apiUrl}/users/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userData)
    });
    await getAllUsers();
}

// Удалить пользователя
async function deleteUser(id) {
    await fetch(`${apiUrl}/users/${id}`, {method: 'DELETE'});
    await getAllUsers();
}

// Отобразить пользователей в таблице (admin)
function displayUsers(users) {
    const tbody = document.querySelector('tbody');
    if (!tbody) return;
    tbody.innerHTML = '';
    users.forEach(user => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => role.roleName).join(' ')}</td>
            <td>
                <button class="btn btn-primary btn-sm" onclick="openEditModal(${user.id})">Edit</button>
            </td>
            <td>
                <button class="btn btn-danger btn-sm" onclick="openDeleteModal(${user.id})">Delete</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}


// Отобразить информацию о текущем пользователе (user)
function displayAuthUser(user) {
    const userInfo = document.querySelector('#user-info');
    if (!userInfo) return;
    userInfo.innerHTML = `
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => role.roleName).join(' ')}</td>
        </tr>
    `;
}

// --- Модальные окна  ---

// Открыть модальное окно редактирования
window.openEditModal = async function (id) {
    // Получаем данные пользователя и все роли
    const response = await fetch(`${apiUrl}/users/${id}`);
    const user = await response.json();
    const roles = await getAllRoles();

    // Удаляем старое модальное окно, если оно есть
    let oldModal = document.getElementById('editModalDynamic');
    if (oldModal) oldModal.remove();

    // Генерируем HTML модального окна
    const modalHtml = `
    <div class="modal fade" id="editModalDynamic" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <form id="formEditUserDynamic">
            <div class="modal-header">
              <h5 class="modal-title">Edit user</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              <input type="hidden" name="id" value="${user.id}">
              <div class="mb-2">
                <label>First name</label>
                <input class="form-control" type="text" name="name" value="${user.name}" required>
              </div>
              <div class="mb-2">
                <label>Last name</label>
                <input class="form-control" type="text" name="lastName" value="${user.lastName}" required>
              </div>
              <div class="mb-2">
                <label>Age</label>
                <input class="form-control" type="number" name="age" value="${user.age}" required>
              </div>
              <div class="mb-2">
                <label>Email</label>
                <input class="form-control" type="email" name="email" value="${user.email}" required>
              </div>
              <div class="mb-2">
                <label>Password</label>
                <input class="form-control" type="password" name="password" placeholder="New password">
              </div>
              <div class="mb-2">
                <label>Roles</label>
                <select class="form-control" name="roles" multiple required>
                  ${roles.map(role => `<option value="${role.id}" ${user.roles.some(r => r.id === role.id) ? 'selected' : ''}>${role.roleName}</option>`).join('')}
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
              <button type="submit" class="btn btn-primary">Save</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    `;
    document.body.insertAdjacentHTML('beforeend', modalHtml);

    // Показываем модальное окно
    const modalEl = document.getElementById('editModalDynamic');
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
    const closeBtn = modalEl.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            modal.hide();
        });
    }

    // Обработка отправки формы
    document.getElementById('formEditUserDynamic').onsubmit = async (e) => {
        e.preventDefault();
        const form = e.target;
        const roles = Array.from(form.roles.selectedOptions).map(opt => ({id: Number(opt.value)}));
        const userData = {
            id: Number(form.id.value),
            name: form.name.value,
            lastName: form.lastName.value,
            age: Number(form.age.value),
            email: form.email.value,
            roles: roles
        };
        if (form.password.value) {
            userData.password = form.password.value;
        }
        await updateUser(id, userData);
        modal.hide();
        modalEl.remove();
    };

    // Удаляем модальное окно из DOM после закрытия
    modalEl.addEventListener('hidden.bs.modal', () => modalEl.remove());
};

// Открыть модальное окно удаления
window.openDeleteModal = async function (id) {
    // Получаем данные пользователя
    const response = await fetch(`${apiUrl}/users/${id}`);
    const user = await response.json();

    // Удаляем старое модальное окно, если оно есть
    let oldModal = document.getElementById('deleteModalDynamic');
    if (oldModal) oldModal.remove();

    // Генерируем HTML модального окна
    const modalHtml = `
    <div class="modal fade" id="deleteModalDynamic" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <form id="formDeleteUserDynamic">
            <div class="modal-header">
              <h5 class="modal-title">Delete user</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              <p>Вы уверены, что хотите удалить пользователя?</p>
              <input type="hidden" name="id" value="${user.id}">
              <div class="mb-2">
                <label>First name</label>
                <input class="form-control" type="text" value="${user.name}" disabled>
              </div>
              <div class="mb-2">
                <label>Last name</label>
                <input class="form-control" type="text" value="${user.lastName}" disabled>
              </div>
              <div class="mb-2">
                <label>Age</label>
                <input class="form-control" type="number" value="${user.age}" disabled>
              </div>
              <div class="mb-2">
                <label>Email</label>
                <input class="form-control" type="email" value="${user.email}" disabled>
              </div>
              <div class="mb-2">
                <label>Roles</label>
                <input class="form-control" type="text" value="${user.roles.map(r => r.roleName).join(' ')}" disabled>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
              <button type="submit" class="btn btn-danger">Delete</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    `;
    document.body.insertAdjacentHTML('beforeend', modalHtml);

    // Показываем модальное окно
    const modalEl = document.getElementById('deleteModalDynamic');
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
    const closeBtn = modalEl.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            modal.hide();
        });
    }

    // Обработка отправки формы
    document.getElementById('formDeleteUserDynamic').onsubmit = async (e) => {
        e.preventDefault();
        await deleteUser(id);
        modal.hide();
        modalEl.remove();
    };

    // Удаляем модальное окно из DOM после закрытия
    modalEl.addEventListener('hidden.bs.modal', () => modalEl.remove());
};
// --- Обработчики форм ---

// Добавление нового пользователя
document.querySelector('form[action*="/create"]')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const roles = Array.from(form.roles.selectedOptions).map(opt => ({id: Number(opt.value)}));
    const userData = {
        name: form.name.value,
        lastName: form.lastName.value,
        age: Number(form.age.value),
        email: form.email.value,
        password: form.password.value,
        roles: roles
    };
    await createUser(userData);
    form.reset();
});

// Сохранение изменений пользователя
document.querySelector('#editModal form')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const id = form.id.value;
    const roles = Array.from(form.roles.selectedOptions).map(opt => ({id: Number(opt.value)}));
    const userData = {
        id: Number(id),
        name: form.name.value,
        lastName: form.lastName.value,
        age: Number(form.age.value),
        email: form.email.value,
        password: form.password.value,
        roles: roles
    };
    await updateUser(id, userData);
    $('#editModal').modal('hide');
});

// Удаление пользователя
document.querySelector('#deleteModal form')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = e.target.id.value;
    await deleteUser(id);
    $('#deleteModal').modal('hide');
});

// --- Инициализация ---

document.addEventListener('DOMContentLoaded', async () => {
    if (window.location.pathname === '/admin') {
        await getAllUsers();
        // Заполнить select ролей для формы добавления
        const roles = await getAllRoles();
        const select = document.querySelector('form[action*="/create"] select[name="roles"]');
        if (select) {
            select.innerHTML = '';
            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.id;
                option.text = role.roleName;
                select.appendChild(option);
            });
        }
    }
    if (window.location.pathname === '/user') {
        await getAuthUser();
    }
});