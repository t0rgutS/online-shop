var product = new Vue({
    el: '#singleProduct',
    template: '<body>' +
    '<shopHeader />' +
    '<template v-if="product != null">' +
    '<div class="d-flex mt-5">' +
    '    <div>' +
    '        <img :src="product.photoURL" alt="Картинки нет..." class="img-fluid ml-3 mr-5"/>' +
    '    </div>' +
    '    <div class="form-group ml-5 mr-5">' +
    '        <fieldset>' +
    '            <span class="text-warning" v-for="index in product.rating" v-on:click="rate(index)">&#9733;</span>' +
    '            <!--span class="text-warning">&#9733;</span-->' +
    '            <!--span class="text-warning">&#9733;</span-->' +
    '            <span v-for="index in 5 - product.rating" v-on:click="rate(product.rating + index)">&#9734;</span>' +
    '            <!--span>&#9734;</span-->' +
    '        </fieldset>' +
    '        <h3>{{ product.productName }}</h3>' +
    '        <p>Цена: <span class="text-primary">{{ product.price }}</span v-if="product.shipType = false"> + доставка</p>' +
    '        <p class="text-success mb-5">В наличии {{ product.count }} штук</p>' +
    '        <p class="mt-5">Продавец: <a href="">{{ product.userLogin }}</a></p>' +
    '        <p>Телефон: ...</p>' +
    '        <p>E-mail: ...</p>' +
    '    </div>' +
    '    <div class="ml-auto mr-3">' +
    '        <div class="card text-center border border-secondary">' +
    '            <div class="card-header">' +
    '                <input type="number" class="form-control mb-2 text-center" placeholder="1">' +
    '                <button class="form-control btn btn-outline-success text-dark">В корзину</button>' +
    '            </div>' +
    '            <div class="card-footer">' +
    '                <button class="form-control btn btn-outline-warning text-dark">В желаемое</button>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>' +
    '<div class="ml-2">' +
    '    <button v-if="product.editable" class="btn btn-outline-secondary mt-2">Сохранить изменения</button>' +
    '    <h6 class="mt-4">Производитель:</h6>' +
    '    <input class="form-control w-50" placeholder="..." v-bind:value="product.producer" disabled="!product.editable">' +
    '    <h6 class="mt-3">Состояние:</h6>' +
    '    <input class="form-control w-50" placeholder="..." v-bind:value="product.condition" disabled="!product.editable">' +
    '    <h6 class="mt-3">Категория:</h6>' +
    '    <input class="form-control w-50" placeholder="..." v-bind:value="product.categoryName" disabled="!product.editable">' +
    '    <h6 class="mt-3">Краткое описание:</h6>' +
    '    <textarea class="form-control w-50" disabled="!product.editable">' +
    '        {{ product.description }}' +
    '    </textarea>' +
    '</div>' +
    '</template>' +
    '</body>',
    data: {
        product: null
    },
    methods: {
        rate: function (rating) {
            //TODO
        }
    },
    created: function () {
        var id = new URL(location.href).searchParams.get('id');
        //TODO
    }
});