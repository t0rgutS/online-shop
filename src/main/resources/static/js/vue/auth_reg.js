Vue.use(VueSession);

var authReg = new Vue({
    el: '#authReg',
    template:
    '<template v-if="showReg">' +
    '<div class="mt-5 ml-auto mr-auto w-25">' +
    '<h5 class="font-weight-bolder text-center">РЕГИСТРАЦИЯ</h5>' +
    '<div class="form-group">' +
    '<p><small>&#9745;</small> Логин: <input class="form-control" v-model="login" placeholder="Логин"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p><small>&#9745;</small> Пароль: <input class="form-control" type="password" v-model="password" ' +
    'placeholder="Минимум 6 символов"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p><small>&#9745;</small> Подтверждение пароля: <input class="form-control" type="password" v-model="confirm"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>Фамилия: <input class="form-control" v-model="surname"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p><small>&#9745;</small> Имя: <input class="form-control" v-model="name"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>Отчество: <input class="form-control" v-model="patronymic"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>Дата рождения: <input class="form-control" type="date" v-model="birthDate"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>Пол: <select class="form-control" v-model="gender">' +
    '<option value="" selected>Не выбрано</option>' +
    '<option value="true">Мужской</option>' +
    '<option value="false">Женский</option>' +
    '</select></p>' +
    '<div class="form-group">' +
    '<p>Город: <select class="form-control" v-model="city">' +
    '<option value="" selected>Не выбрано</option> ' +
    '</select></p>' +
    '<div class="form-group">' +
    '<p>Телефон: <input class="form-control" type="tel" v-model="phone"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>E-mail: <input class="form-control" type="email" v-model="email"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>Фото профиля: <input class="form-control-file" type="file"></p>' +
    '</div>' +
    '<!--div class="form-group"-->' +
    '<button class="btn btn-dark mt-3">Зарегистрироваться</button>' +
    '<div class="mt-3 mb-3">' +
    '<p>Уже есть аккаунт? <a href="#" v-on:click="showReg = false">Войти</a></p>' +
    '</div>' +
    '<!--/div-->' +
    '</div>' +
    '</template>' +
    '<template v-else>' +
    '<div class="mt-5 ml-auto mr-auto w-25">' +
    '<h5 class="font-weight-bolder text-center mb-3">АВТОРИЗАЦИЯ</h5>' +
    '<div class="form-group">' +
    '<input type="text" class="form-control" v-model="login" placeholder="Логин" autofocus>' +
    '</div>' +
    '<div class="form-group">' +
    '<input type="password" class="form-control" v-model="password" placeholder="Пароль">' +
    '</div>' +
    '<div class="d-flex justify-content-between">' +
    '<button type="submit" class="btn btn-success" v-on:click="authorize">Войти</button>' +
    '<button class="btn btn-dark" v-on:click="showReg = true">Зарегистрироваться</button>' +
    '</div>' +
    '</div>' +
    '</template>',
    data: function () {
        return {
            showReg: false,
            login: '',
            password: '',
            confirm: '',
            surname: '',
            name: '',
            patronymic: '',
            birthDate: '',
            gender: '',
            city: '',
            phone: '',
            email: '',
            photo: ''
        }
    },
    methods: {
        authorize: function () {
            //this.$session.start();
        },
        register: function () {

        }
    }
});