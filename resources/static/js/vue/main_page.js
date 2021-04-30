var main = new Vue({
    el: '#productList',
    template: '<div>' +
    '<shopHeader v-on:categorySet="setCategory" v-on:searchSet="setSearch" />' +
    '<errorAlert :error="error" />' +
    '<product :product="product" v-on:update="updateProduct" />' +
    '<div class="container">' +
    '<div class="row">' +
    '<div class="col-sm-8 col-lg-4" v-for="product in products">' +
    '<div class="card mt-3 mb-3">' +
    '<div class="card-header text-center">' +
    '<h5 class="card-title">{{ product.productName }}</h5>' +
    '</div>' +
    '<div class="card-body text-center">' +
    '<img class="card-img-top img-fluid" :src="product.photoURL" alt="Картинки нет...">' +
    '</div>' +
    '<div class="card-footer">' +
    '<p class="card-text text-primary">{{ product.price }}&#8381;</p>' +
    '<small><p class="card-text text-success" v-if="product.condition">Новое!</p>' +
    '<p class="card-text text-danger" v-else>Б/у</p>' +
    '<p class="card-text">Количество: {{ product.count }} шт.</p></small>' +
    '<div class="text-center mt-3">' +
    '<button class="form-control btn btn-outline-success text-dark" v-on:click="open(product.id)">Открыть</button>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '<basicFooter :page="page" v-on:changePage="onPageChanged" v-on:applyFilters="onFiltersApplied" />' +
    '</div>',
    data: function () {
        return {
            products: [],
            product: null,
            search: null,
            page: 1,
            shipType: null,
            condition: null,
            price: null,
            priceOp: '=',
            count: null,
            countOp: null,
            category: null,
            error: null
        }
    },
    methods: {
        resetFilters: function () {
            search = null;
            shipType = null;
            condition = null;
            price = null;
            count = null;
            category = null;
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
                    category: this.category,
                    page: (this.page - 1)
                }
            })
                .then((response) => {
                    if(response.ok) {
                        response.json().then(data => data.content.forEach(product => this.products.push(product)));
                    }
                }, (error) => {
                    alert("Ошибка: " + error.body.message);
                });
        },
        setCategory: function (newCategory) {
            this.category = newCategory;
            this.getProducts();
        },
        setSearch: function (newSearchString) {
            this.searchString = newSearchString;
            this.getProducts();
        },
        updateProduct: function (oldProduct, newProduct) {
            var ind = this.products.indexOf(oldProduct);
            this.products[ind] = newProduct;
           // this.clearSelected();
            $('#productModal').modal('hide');
        },
        onPageChanged: function (value) {
            if(!(this.page == 1 && value == -1)) {
                this.page += value;
                this.getProducts();
            }
        },
        onFiltersApplied: function (shipType, condition, price, priceOp, count, countOp) {
            $('#filterModal').modal('hide');
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
                    alert("Ошибка: " + error.body.message);
                });
            $('#productModal').modal('show');
        }
    },
    created: function () {
        this.getProducts()
    }
});