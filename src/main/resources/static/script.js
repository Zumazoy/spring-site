document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('[th\\:onclick^="confirmDelete"]').forEach(btn => {
        btn.addEventListener('click', function() {
            const userId = this.getAttribute('th:onclick').match(/\d+/)[0];
            confirmDelete(userId);
        });
    });
});

function confirmDelete(userId) {
    const form = document.getElementById('delete-form-' + userId);
    form.submit();
}

function showErrorNotification(errors) {
    const errorNotification = document.getElementById('errorNotification');
    const errorList = document.getElementById('errorList');

    if (!errorNotification || !errorList) return;

    errorList.innerHTML = '';
    document.querySelectorAll('.error-field').forEach(el => {
        el.classList.remove('error-field');
    });

    for (const [field, message] of Object.entries(errors)) {
        const li = document.createElement('li');
        li.textContent = message;
        errorList.appendChild(li);

        const input = document.querySelector(`[name="${field}"], [th\\:field="*{${field}}"]`);
        if (input) {
            input.classList.add('error-field');
            if (errorList.children.length === 1) {
                input.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }
    }

    errorNotification.style.display = 'block';
    setTimeout(() => {
        if (errorNotification.style.display === 'block') {
            closeErrorNotification();
        }
    }, 10000);
}

function showUpdateErrorNotification(errors) {
    const notification = document.getElementById('updateErrorNotification');
    const errorList = document.getElementById('updateErrorList');

    if (!notification || !errorList) return;

    errorList.innerHTML = '';
    for (const [field, message] of Object.entries(errors)) {
        const li = document.createElement('li');
        li.textContent = message;
        errorList.appendChild(li);
    }

    notification.style.display = 'block';

    setTimeout(() => {
        if (notification.style.display === 'block') {
            closeUpdateErrorNotification();
        }
    }, 10000);
}

function showDeleteErrorNotification(errors) {
    const notification = document.getElementById('deleteErrorNotification');
    const errorList = document.getElementById('deleteErrorList');

    if (!notification || !errorList) return;

    errorList.innerHTML = '';
    const li = document.createElement('li');
    li.textContent = errors;
    errorList.appendChild(li);

    notification.style.display = 'block';

    setTimeout(() => {
        if (notification.style.display === 'block') {
            closeUpdateErrorNotification();
        }
    }, 10000);
}

function closeErrorNotification() {
    const notification = document.getElementById('errorNotification');
    if (notification) {
        notification.style.display = 'none';
        document.querySelectorAll('.add-error-field').forEach(el => {
            el.classList.remove('add-error-field');
        });
    }
}

function closeUpdateErrorNotification() {
    const notification = document.getElementById('updateErrorNotification');
    if (notification) {
        notification.style.display = 'none';
        document.querySelectorAll('.update-error-field').forEach(el => {
            el.classList.remove('update-error-field');
        });
    }
}


document.addEventListener('DOMContentLoaded', function() {
    const loginError = document.getElementById('errorNotification');
    if (loginError) {
        setTimeout(() => {
            closeErrorNotification();
        }, 10000);
    }

    const registrationErrors = document.getElementById('errorNotification');
    if (registrationErrors) {
        highlightErrorFields();
        setTimeout(() => {
            closeErrorNotification();
        }, 10000);
    }
});

function highlightErrorFields() {
    const errorList = document.getElementById('errorList');
    if (!errorList) return;

    const errors = {};
    errorList.querySelectorAll('li').forEach(li => {
        const text = li.textContent;
        if (text.includes('Логин')) errors['login'] = text;
        if (text.includes('Email')) errors['email'] = text;
        if (text.includes('Номер телефона')) errors['phoneNumber'] = text;
        if (text.includes('Имя')) errors['name'] = text;
        if (text.includes('Фамилия')) errors['surname'] = text;
        if (text.includes('Пароль')) errors['password'] = text;
    });

    for (const field in errors) {
        const input = document.querySelector(`input[name="${field}"]`);
        if (input) {
            input.classList.add('add-error-field');
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const isHomePage = window.location.pathname === '/';

    if (isHomePage) {
        const authInfo = document.getElementById('auth-info');
        const isAuthenticated = authInfo && authInfo.dataset.isAuthenticated === 'true';

        if (isAuthenticated) {
            document.addEventListener('keydown', function(e) {
                if (e.ctrlKey && e.shiftKey && e.key === 'S') {
                    const button = document.getElementById('secret-admin-button');
                    if (button) {
                        button.style.display = button.style.display === 'none' ? 'block' : 'none';
                    }
                }
            });
            document.addEventListener('keydown', function(e) {
                if (e.ctrlKey && e.shiftKey && e.key === 'X') {
                    const button = document.getElementById('secret-schedule-button');
                    if (button) {
                        button.style.display = button.style.display === 'none' ? 'block' : 'none';
                    }
                }
            });
        }
    }
});