Vue.use(VueSession);

var authReg = new Vue({
    el: '#authReg',
    template:
    '<div>' +
    '<div v-if="showReg">' +
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
    '<!--div class="form-group">' +
    '<p>Фамилия: <input class="form-control" v-model="surname"></p>' +
    '</div-->' +
    '<div class="form-group">' +
    '<p><small>&#9745;</small> Имя в системе: <input class="form-control" v-model="name"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>Страна: <select class="form-control" v-model="country" v-on:change="loadCities">' +
    '<option value="null" selected>Не выбрано</option>' +
    '<option v-for="country in countries"> {{ country.country }} </option>' +
    '</select></p>' +
    '</div>' +
    '<datalist id="cityList">' +
    '<option v-for="city in cities">{{ city.city }}</option>' +
    '</datalist>' +
    '<div class="form-group">' +
    '<p>Город: <input class="form-control" list="cityList" v-model="city"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>Телефон: <input class="form-control" type="tel" v-model="phone"></p>' +
    '</div>' +
    '<div class="form-group">' +
    '<p>E-mail: <input class="form-control" type="email" v-model="email"></p>' +
    '</div>' +
    '<!--div class="form-group"-->' +
    '<button class="btn btn-dark mt-3" v-on:click="register">Зарегистрироваться</button>' +
    '<div class="mt-3 mb-3">' +
    '<p>Уже есть аккаунт? <a href="#" v-on:click="showReg = false">Войти</a></p>' +
    '</div>' +
    '<!--/div-->' +
    '</div>' +
    '</div>' +
    '<div v-else>' +
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
    '<button class="btn btn-dark" v-on:click="goToReg">Зарегистрироваться</button>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>',
    data: function () {
        return {
            showReg: false,
            countries: [],
            cities: [],
            login: null,
            password: null,
            confirm: null,
            name: null,
            city: null,
            country: null,
            phone: null,
            email: null
        }
    },
    methods: {
        authorize: function () {
            this.$http.post('http://localhost:8080/api/users/auth', {
			    login: this.login,
			    password: this.password
		    }).then((response) => {
		        if(response.ok) {
			        this.$session.start();
			        this.$session.set('login', response.body.login);
			        this.$session.set('token', response.body.token);
			        if(window.history.length > 0)
				        window.history.go(-1);
			        else
				        window.location.href='index.html';
		        }
                    console.log(response)
            }).catch(error => {
                alert("Ошибка: " + error.body.message);
            });
        },
        register: function () {
            if(this.login == null || this.login == '')
                this.error = 'Укажите логин!';
            if(this.password == null || this.password == '')
                this.error = 'Введите пароль!';
            if(this.password != this.confirm)
                this.error = 'Пароли не совпадают!';
            if(this.name == null || this.name == '')
                this.error = 'Укажите имя!';
            else
                this.$http.post('http://localhost:8080/api/users/create', {
                    login: this.login,
                    password: this.password,
                    name: this.name,
                    cityName: this.city,
                    countryName: this.country,
                    phone: this.phone,
                    email: this.email
                }).then(response => {
                    if(response.ok) {
                        this.confirm = null;
                        this.name = null;
                        this.city = null;
                        this.country = null;
                        this.phone = null;
                        this.email = null;
                        this.showReg = false;
                    }
                }).catch(error => {
                    alert("Ошибка: " + error.body.message);
                });
        },
        loadCities: function () {
            if(this.cities.length == 0)
                this.cities.splice(0);
            if(this.country != null)
                this.$http.get('http://localhost:8080/api/utils/cities/' + this.country)
                .then(response => {
                    if(response.ok) {
                        response.json().then(data => data.forEach(city => this.cities.push(city)));
                    }
                }).catch(error => {
                    console.log(error);
                });
        },
        goToReg: function () {
            if(this.countries.length == 0) {
                this.$http.get('http://localhost:8080/api/utils/countries')
                    .then(response => {
                        if(response.ok) {
                            response.json().then(data => data.forEach(country => this.countries.push(country)));
                        }
                    }).catch(error => {
                        alert("Ошибка: " + error.body.message);
                    });
            }
            this.showReg = true;
        }
    }
});