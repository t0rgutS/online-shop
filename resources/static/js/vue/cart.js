var cart = new Vue({
    el: '#cartList',
    template: '<div>' +
    '<shopHeader v-on:categorySet="setCategory" v-on:searchSet="setSearch" />' +
    '<errorAlert :error="error" />' +
    '<product :product="product" v-on:update="updateProduct" />' +
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
    '        <cartComponent v-for="cartwish in cartwishes" :cartwish="cartwish" v-on:remove="remove" v-on:update="updateCart" v-on:error="setError" />' +
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
    '                    <button type="button" class="btn btn-outline-danger close" aria-label="Close" v-on:click="remove(cartwish)">' +
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
    '                        <button class="form-control btn btn-outline-success text-dark" v-on:click="open(cartwish)">' +
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
            cartwish: null,
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
            cart: true,
            error: null
        }
    },
    watch: {
      cart: function () {
          this.getProducts();
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
        setCategory: function (newCategory) {
            this.category = newCategory;
            this.getProducts();
        },
        setSearch: function (newSearchString) {
            this.searchString = newSearchString;
            this.getProducts();
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
        getProducts: function () {
            if(this.cartwishes.length > 0)
                this.cartwishes.splice(0);
            this.$http.get('http://localhost:8080/api/cartwish/' + (this.cart ? 'cart' : 'wishlist'), {
                    params: {
                        searchString: this.search,
                        priceOp: this.priceOp,
                        price: this.price,
                        condition: this.condition,
                        shipType: this.shipType,
                        countOp: this.countOp,
                        count: this.count,
                        page: (this.page - 1)
                    },  headers: {
                        Authorization: 'Bearer ' + this.$session.get('token')
                    }
                })
                    .then(response => {
                        console.log(response);
                        if(response.ok) {
                            response.json().then(data => data.content.forEach(cartwish => this.cartwishes.push(cartwish)));
                }
            })
                    .catch(error => {
                        if(error.status == 403)
                            window.location.href = "login.html";
                        alert("Ошибка: " + error.body.message);
                });

        },
        updateProduct: function (oldProduct, newProduct) {
            this.cartwish.product = newProduct;
            $('#productModal').modal('hide');
        },
        updateCart: function (oldCart, newCart) {
            var ind = this.cartwishes.indexOf(oldCart);
            this.cartwishes[ind] = newCart;
        },
        open: function (cartwish) {
            this.cartwish = cartwish;
            this.product = cartwish.product;
            $('#productModal').modal('show');
        },
        remove: function (cartwish) {
            this.$http.delete('http://localhost:8080/api/cartwish/delete/' + cartwish.id, {
                headers: {
                    Authorization: 'Bearer ' + this.$session.get('token')
                }
            }).then(response => {
                if(response.ok)
                    this.cartwishes.splice(this.cartwishes.indexOf(cartwish), 1);
                else
                    alert("Неизвестная ошибка!");
            }).catch(error => {
                alert("Ошибка: " + error.body.message);
            });
        },
        setError: function (error) {
            this.error = error;
            $('#errorModal').modal('show');
        }
    },
    created: function () {
        this.getProducts();
    }
});