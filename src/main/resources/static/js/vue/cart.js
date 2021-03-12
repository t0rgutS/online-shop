var cart = new Vue({
    template: '<body style="background-color: #C4C4C4">' +
    '<shopHeader />' +
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
    '        <div class="col-sm-8 col-lg-4" v-for="product in products">' +
    '            <div class="card mt-3 mb-3">' +
    '                <div class="card-header text-center">' +
    '                    <button type="button" class="btn btn-outline-danger close" aria-label="Close">' +
    '                        <span aria-hidden="true">&times;</span>' +
    '                    </button>' +
    '                    <h5 class="card-title">{{ product.productName }}</h5>' +
    '                </div>' +
    '                <div class="card-body text-center">' +
    '                    <img class="card-img-top img-fluid"' +
    '                         src="{{ product.photoURL }}" alt="Картинки нет...">' +
    '                </div>' +
    '                <div class="card-footer">' +
    '                    <p class="card-text text-primary">{{ product.price }}</p>' +
    '                    <div class="form-group mt-3">' +
    '                        <input type="number" class="form-control mb-2 text-center mx-auto" ' +
    '                           placeholder="1" min="1" value="{{ product.count }}">' +
    '                        <button class="form-control btn btn-outline-success text-dark">' +
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
    '        <div class="col-sm-8 col-lg-4" v-for="product in products">' +
    '            <div class="card mt-3 mb-3">' +
    '                <div class="card-header text-center">' +
    '                    <button type="button" class="btn btn-outline-danger close" aria-label="Close">' +
    '                        <span aria-hidden="true">&times;</span>' +
    '                    </button>' +
    '                    <h5 class="card-title">{{ product.productName }}</h5>' +
    '                </div>' +
    '                <div class="card-body text-center">' +
    '                    <img class="card-img-top img-fluid"' +
    '                         src="{{ product.photoURL }}" alt="Картинки нет...">' +
    '                </div>' +
    '                <div class="card-footer">' +
    '                    <p class="card-text text-primary">{{ product.price }}</p>' +
    '                    <div class="text-center mt-3">' +
    '                        <button class="form-control btn btn-outline-success text-dark">' +
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
    '</body>',
    data: function () {
        return {
            products: [],
            cart: true
        }
    },
    methods: {
        onPageChanged: function () {

        },
        onFiltersApplied: function () {

        }
    }
});