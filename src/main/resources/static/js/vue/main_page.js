var main = new Vue({
    el: '#productList',
    template: '<body style="background-color: #C4C4C4">' +
    '<shopHeader />' +
    '<div class="container">' +
    '<div class="row">' +
    '<div class="col-sm-8 col-lg-4" v-for="product in products">' +
    '<div class="card mt-3 mb-3">' +
    '<div class="card-header text-center">' +
    '<h5 class="card-title">{{ product.productName }}</h5>' +
    '</div>' +
    '<div class="card-body text-center">' +
    '<img class="card-img-top img-fluid" src="{{ product.photoURL }}" alt="Картинки нет...">' +
    '</div>' +
    '<div class="card-footer">' +
    '<p class="card-text text-primary">{{ product.price }}</p>' +
    '<div class="text-center mt-3">' +
    '<button class="form-control btn btn-outline-success text-dark" value="{{ product.id }}">Открыть</button>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '<basicFooter v-on:changePage="onPageChanged" v-on:applyFilters="onFiltersApplied" />' +
    '</body>',
    data: function () {
        return {
            products: [],
            search: '',
            page: 1
        }
    },
    methods: {
        getProducts: function () {
            //TODO
        },
        onPageChanged: function (value) {
            //TODO
            this.page += value;
            this.getProducts();
        },
        onFiltersApplied: function (shipType, condition, price, priceOp) {
            //TODO
            this.getProducts();
        }
    },
    created: function () {
        this.getProducts()
    }
});