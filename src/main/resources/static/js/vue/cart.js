var cart = new Vue({
    el: '#cartList',
    template: '<div>' +
    '<shopHeader />' +
    '<product :product="product" />' +
    '<template v-if="cart == true">' +
    '<ul class="nav nav-tabs ml-2">' +
    '    <li class="nav-item">' +
    '        <a class="nav-link active" aria-current="page" href="#">Корзина</a>' +
    '    </li>' +
    '    <li class="nav-item">' +
    '        <a class="nav-link" href="#" v-on:click="cart = false">Желаемое</a>' +
    '    </li>' +
    '</ul>' +
    '<div class="container">' +
    '    <div class="row">' +
    '        <div class="col-sm-8 col-lg-4" v-for="cartwish in cartwishes">' +
    '            <div class="card mt-3 mb-3">' +
    '                <div class="card-header text-center">' +
    '                    <button type="button" class="btn btn-outline-danger close" aria-label="Close">' +
    '                        <span aria-hidden="true">&times;</span>' +
    '                    </button>' +
    '                    <h5 class="card-title">{{ cartwish.product.productName }}</h5>' +
    '                </div>' +
    '                <div class="card-body text-center">' +
    '                    <img class="card-img-top img-fluid"' +
    '                         v-bind:src="cartwish.product.photoURL" alt="Картинки нет...">' +
    '                </div>' +
    '                <div class="card-footer">' +
    '                    <p class="card-text text-primary">{{ cartwish.product.price }}</p>' +
    '                    <div class="form-group mt-3">' +
    '                        <input type="number" class="form-control mb-2 text-center mx-auto" ' +
    '                           placeholder="1" min="1" v-bind:value="cartwish.count">' +
    '                        <button class="form-control btn btn-outline-success text-dark" v-on:click="open(cartwish.product)">' +
    '                            Открыть' +
    '                        </button>' +
    '                    </div>' +
    '                </div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>' +
    '<cartFooter v-on:changePage="onPageChanged" />' +
    '</template>' +
    '<template v-else>' +
    '<ul class="nav nav-tabs ml-2">' +
    '    <li class="nav-item">' +
    '        <a class="nav-link" href="#" v-on:click="cart = true">Корзина</a>' +
    '    </li>' +
    '    <li class="nav-item">' +
    '        <a class="nav-link active" aria-current="page" href="#">Желаемое</a>' +
    '    </li>' +
    '</ul>' +
    '<div class="container">' +
    '    <div class="row">' +
    '        <div class="col-sm-8 col-lg-4" v-for="cartwish in cartwishes">' +
    '            <div class="card mt-3 mb-3">' +
    '                <div class="card-header text-center">' +
    '                    <button type="button" class="btn btn-outline-danger close" aria-label="Close">' +
    '                        <span aria-hidden="true">&times;</span>' +
    '                    </button>' +
    '                    <h5 class="card-title">{{ cartwish.product.productName }}</h5>' +
    '                </div>' +
    '                <div class="card-body text-center">' +
    '                    <img class="card-img-top img-fluid"' +
    '                         v-bind:src="cartwish.product.photoURL" alt="Картинки нет...">' +
    '                </div>' +
    '                <div class="card-footer">' +
    '                    <p class="card-text text-primary">{{ cartwish.product.price }}</p>' +
    '                    <div class="text-center mt-3">' +
    '                        <button class="form-control btn btn-outline-success text-dark" v-on:click="open(cartwish.product)">' +
    '                            Открыть' +
    '                        </button>' +
    '                    </div>' +
    '                </div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>' +
    '<basicFooter v-on:changePage="onPageChanged" v-on:applyFilters="onFiltersApplied" />' +
    '</template>' +
    '</div>',
    data: function () {
        return {
            cartwishes: [],
            product: null,
            search: null,
            page: 1,
            shipType: null,
            condition: null,
            price: null,
            priceOp: '=',
            count: null,
            countOp: null,
            cart: true
        }
    },
    methods: {
        onPageChanged: function () {

        },
        onFiltersApplied: function () {

        },
        getProducts: function () {
            if(this.cartwishes.length > 0)
                this.cartwishes.splice(0);
            url = '';
            this.$http.get('http://localhost:8080/api/cartwish/' + (this.cart ? 'cart' : 'wishlist'), {
                    params: {
                        searchString: this.search,
                        priceOp: this.priceOp,
                        price: this.price,
                        condition: this.condition,
                        shipType: this.shipType,
                        countOp: this.countOp,
                        count: this.count,
                        page: (this.page - 1),
                        login: null
                    }
                })
                    .then(response => {
                        if(response.ok) {
                            response.json().then(data => data.content.forEach(cartwish => this.cartwishes.push(cartwish)));
                }
            })
                    .catch(error => {
                        if(error.status == 403)
                            window.location.href = "login.html";
                        console.log(error.body.message);
                });

        },
        open: function (product) {
            this.product = product;
            $('#productModal').modal('show');
        }
    },
    created: function () {
        this.getProducts();
    }
});