Vue.use(VueSession);

var cab = new Vue({
    el: "#userCab",
    template: '<div v-if="user != null">' +
    '<shopHeader />' +
    '<addProduct v-if="user.editable" />' +
    '<changePass v-if="user.editable" :id="user.id" />' +
    '<deleteAcc v-if="user.editable" :text="\'Вы уверены, что хотите удалить свой аккаунт? Отменить это действие будет невозможно!\'" v-on:deleteAnyway="deleteAccount" />' +
    '<product :product="product" />' +
    '<div class="d-flex justify-content-between mt-3">' +
    '    <div class="form-group ml-5">' +
    '       <button class="btn btn-outline-danger mb-4" v-if="user.editable" v-on:click="exit">Выйти</button>' +
    '       <fieldset>' +
    '           <span class="text-warning" v-for="index in user.rating">&#9733;</span>' +
    '           <span v-for="index in 5 - user.rating">&#9734;</span>' +
    '        </fieldset>' +
    '        <h2 class="text-primary mb-3">{{ user.login }}</h2>' +
    '        <datalist id="cityList" v-if="user.editable">' +
    '           <option v-for="city in cities" v-bind:value="city.id">{{ city.city }} ({{ city.country.country }})</option>' +
    '        </datalist>' +
    '        <p>Название: <input class="form-control" v-bind:value="user.name" :readonly="!user.editable"></p>' +
    '        <p>Местоположение: <input class="form-control" list="cityList" v-bind:value="getLocation" :readonly="!user.editable"></p>' +
    '        <p>Телефон: <input class="form-control" v-bind:value="user.phone" :readonly="!user.editable"></p>' +
    '        <p>E-mail: <input class="form-control" v-bind:value="user.email" :readonly="!user.editable"></p>' +
    '        <div class="form-inline">' +
    '            <button class="btn btn-outline-secondary text-dark mr-5" v-on:click="showChangePass" v-if="user.editable">Сменить пароль</button>' +
    '            <button class="btn btn-outline-danger text-dark" v-on:click="showDeleteAcc" v-if="user.editable">Удалить аккаунт</button>' +
    '        </div>' +
    '    </div>' +
    '    <div>' +
    '        <img src="../static/images/user_icon.png" class="img-fluid mr-5"/>' +
    '    </div>' +
    '</div>' +
    '<div class=" form-inline mt-5">' +
    '    <button class="btn btn-outline-success text-dark ml-3" v-on:click="addProduct" v-if="user.editable">+ Добавить</button>' +
    '    <h2 class="text-dark mx-auto">ТОВАРЫ</h2>' +
    '</div>' +
    '<table class="table table-bordered text-center mt-2">' +
    '    <thead class="table-secondary">' +
    '    <tr>' +
    '        <th scope="col">' +
    '            Название' +
    '        </th>' +
    '        <th scope="col">' +
    '            Производитель' +
    '        </th>' +
    '        <th scope="col">' +
    '            Цена (руб)' +
    '        </th>' +
    '        <th scope="col">' +
    '            Количество' +
    '        </th>' +
    '        <th scope="col"></th>' +
    '        <th scope="col" v-if="user.editable"></th>' +
    '    </tr>' +
    '    </thead>' +
    '    <tbody>' +
    '    <tr v-for="product in products">' +
    '        <td scope="col">' +
    '            {{ product.productName }}' +
    '        </td>' +
    '        <td scope="col">' +
    '            {{ product.producer.producerName }}' +
    '        </td>' +
    '        <td scope="col">' +
    '            {{ product.price }}' +
    '        </td>' +
    '        <td scope="col">' +
    '            {{ product.count }}' +
    '        </td>' +
    '        <td scope="col">' +
    '            <button class="btn btn-outline-success" v-on:click="open(product.id)">Открыть</button>' +
    '        </td>' +
    '        <td scope="col" v-if="user.editable">' +
    '            <button class="btn btn-outline-danger" v-on:click="deleteProduct(product)" v-if="user.editable">Удалить</button>' +
    '        </td>' +
    '    </tr>' +
    '    </tbody>' +
    '</table>' +
    '<basicFooter :page="page" v-on:changePage="onPageChanged" v-on:applyFilters="onFiltersApplied" />' +
    '</div>',
    data: function () {
        return {
            user: null,
            cities: [],
            products: [],
            product: null,
            search: null,
            page: 1,
            shipType: null,
            condition: null,
            price: null,
            priceOp: '=',
            count: null,
            countOp: null
        }
    },
    computed: {
        getLocation: function () {
            if(this.user.city != null) {
                return this.user.city.id;
            } else
                return "";
        }
    },
    methods: {
        save: function () {

        },
        addProduct: function () {
            $('#addModal').modal('show');
        },
        showChangePass: function () {
            $('#changePassModal').modal('show');
        },
        showDeleteAcc: function () {
            $('#delAccModal').modal('show');
        },
        onPageChanged: function (value) {
            if(!(this.page == 1 && value == -1)) {
                this.page += value;
                this.getProducts();
            }
        },
        onFiltersApplied: function (shipType, condition, price, priceOp, count, countOp) {
            this.shipType = shipType;
            this.condition = condition;
            this.price = price;
            this.priceOp = priceOp;
            this.count = count;
            this.countOp = countOp;
            this.getProducts();
        },
        open: function (id) {
            this.$http.get('http://localhost:8080/api/products/get/' + id, {
                headers: {
                    Authorization: 'Bearer ' + this.$session.get('token')
                }
            })
                .then(response => {
                    if(response.ok) {
                        response.json().then(data => this.product = data);
            }
            })
                .catch(error => {
                    if(error.status == 403)
                        window.location.href = 'login.html';
                    console.log(error.body.message);
            });
            $('#productModal').modal('show');
        },
        deleteProduct: function (product) {
            this.$http.delete('http://localhost:8080/api/products/delete/' + product.id, {
                headers: {
                    Authorization: 'Bearer ' + this.$session.get('token')
                }
            })
                .then(response => {
                    if(response.ok) {
                        this.products.removeChild(product);
                    }
                })
                .catch(error => {
                    if(error.status == 403)
                        window.location.href = 'login.html';
                    console.log(error.body.message);
                });
        },
        exit: function () {
            this.$session.destroy();
            window.location.href = 'index.html';
        },
        deleteAccount: function () {

        },
        getProducts: function () {
            if(this.products.length > 0)
                this.products.splice(0);
            this.$http.get('http://localhost:8080/api/products/get', {
                params: {
                    searchString: this.search,
                    priceOp: this.priceOp,
                    price: this.price,
                    condition: this.condition,
                    shipType: this.shipType,
                    countOp: this.countOp,
                    count: this.count,
                    page: (this.page - 1),
                    currentUser: true
                }, headers: {
                    Authorization: 'Bearer ' + this.$session.get('token')
                }
            })
                .then((response) => {
                    if(response.ok) {
                        response.json().then(data => data.content.forEach(product => this.products.push(product)));
                    }
                })
                .catch(error => {
                    if(error.status == 403)
                        window.location.href = 'login.html';
                    console.log(error.body.message);
                });
        }
    },
    created: function () {
        var param = new URLSearchParams(window.location.search);
        if(!param.has('login'))
            if(window.history.length > 0)
                window.history.go(-1);
            else
                window.location.href='index.html';
        this.$http.get('http://localhost:8080/api/users/get/' + param.get('login'), {
            headers: {
                Authorization: 'Bearer ' + this.$session.get('token')
            }
        })
            .then(response => {
                if(response.ok) {
                    console.log(response);
                    response.json().then(data => this.user = data);
                    this.getProducts();
                    if(this.user.editable)
                        this.$http.get('http://localhost:8080/api/utils/cities')
                            .then(response => {
                                if(response.ok) {
                                    response.json().then(data => data.forEach(city => this.cities.push(city)));
                                }
                            })
                            .catch(error => {
                                console.log(error.body.message);
                            });
                }
            })
            .catch(error => {
                if(error.status == 403)
                   window.location.href = 'login.html';
                console.log(error.body.message);
            });
    }
});