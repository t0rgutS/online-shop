Vue.component('shopHeader', {
    template:
    '<header>' +
    '    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">' +
    '        <div class="d-flex flex-md-fill">' +
    '            <a class="navbar-brand" href="#">' +
    '                <img src="../images/beta_icon.png" width="60" height="40" alt="Логотипище...">' +
    '            </a>' +
    '            <a class="nav-link dropdown-toggle common-color" href="#" id="navbarDropdown" role="button"' +
    '               data-toggle="dropdown"' +
    '               aria-haspopup="true" aria-expanded="false">' +
    '                Категории' +
    '            </a>' +
    '            <div class="dropdown-menu" aria-labelledby="navbarDropdown">' +
    '                <a class="dropdown-item" href="#">Все</a>' +
    '                <a class="dropdown-item" href="#">Говно</a>' +
    '                <a class="dropdown-item" href="#">Не говно</a>' +
    '            </div>' +
    '            <input class="form-control mr-sm-2" type="search" placeholder="Поиск" aria-label="Search">' +
    '            <img src="../images/search_icon.png" width="50" height="40" alt="Найти" class="btn btn-danger">' +
    '            <a href="cart.html">' +
    '                <img src="../images/bucket_icon.png" class="ml-3" width="50" height="40" alt="">' +
    '                <!--span class="common-color ml-1">Корзина</span-->' +
    '            </a>' +
    '            <a href="login.html" class="ml-4 common-color" v-on:click="onHeaderLabelPressed">' +
    '               {{ getHeaderLabel }}' +
    '            </a>' +
    '        </div>' +
    '    </nav>' +
    '</header>',
    data: function () {
        return {}
    },
    computed: {
        getHeaderLabel: function () {
            if (this.$session.exists())
                return this.$session.get('login');
            else
                return 'Войти';
        }
    },
    methods: {
        onHeaderLabelPressed: function () {
            if (this.$session.exists())
                this.$router.push('/cabinet.html');
            else
                this.$router.push('/login.html');
        }
    }
});

Vue.component('filters', {
    template: '<div class="modal fade bd-example-modal-sm" id="filterModal" tabindex="-1" role="dialog" ' +
    'aria-hidden="true">' +
    '  <div class="modal-dialog modal-sm">' +
    '    <div class="modal-content">' +
    '      <div class="form-group">' +
    '           <p> Доставка: <select class="form-control" v-model="shipType">' +
    '               <option value="" selected>Не выбрано</option>' +
    '               <option value="true">За счет продавца</option>' +
    '               <option value="false">За счет покупателя</option>' +
    '           </select></p>' +
    '      </div>' +
    '      <div class="form-group mt-2">' +
    '           <p> Состояние: <select class="form-control" v-model="condition">' +
    '               <option value="" selected>Не выбрано</option>' +
    '               <option value="true">Новое</option>' +
    '               <option value="false">Б/у</option>' +
    '           </select></p>' +
    '      </div>' +
    '      <div class="form-group mt-2">' +
    '           <p> Цена: <select class="form-control" v-model="priceOp">' +
    '               <option value=">" selected></option>' +
    '               <option value="<">&lt;</option>' +
    '               <option value=">=">&gt;=</option>' +
    '               <option value="<=">&lt;=</option>' +
    '               <option value="=">=</option>' +
    '           </select>' +
    '           <input type="number" class="form-control" v-model="price">' +
    '           </p>' +
    '      </div>' +
    '    </div>' +
    '    <button class="mt-3 btn btn-outline-secondary mx-auto" v-on="$emit(\'applyFilters\', shipType, ' +
    '                       condition, price, priceOp)">Применить</button>' +
    '  </div>' +
    '</div>'
});

Vue.component('basicFooter', {
    props: ['page'],
    template: '<filters />' +
    '<footer class="mt-3 mb-5">' +
    '    <div class="d-flex justify-content-between">' +
    '        <div class="form-inline ml-4">' +
    '            <button class="btn btn-outline-dark mr-3" v-on:click="$emit(\'changePage\', -1)">&lt;</button>' +
    '            Страница {{ page }}' +
    '            <button class="btn btn-outline-dark ml-3" v-on:click="$emit(\'changePage\', 1)">&gt;</button>' +
    '        </div>' +
    '        <button class="btn btn-outline-info text-dark mr-4" ' +
    'data-toggle="modal" data-target="#filterModal">Фильтры</button>' +
    '    </div>' +
    '</footer>'
});

Vue.component('cartFooter', {
    props: ['page', 'totalCost'],
    template: '<footer class="mt-3 mb-5">' +
    '    <div class="d-flex justify-content-between">' +
    '        <div class="form-inline ml-4">' +
    '            <button class="btn btn-outline-dark mr-3" v-on:click="$emit(\'changePage\', -1)">&lt;</button>' +
    '            Страница {{ page }}' +
    '            <button class="btn btn-outline-dark ml-3" v-on:click="$emit(\'changePage\', 1)">&gt;</button>' +
    '        </div>' +
    '        <div class="form-group mr-4">' +
    '            <h5>Общая стоимость: <span class="text-primary">{{ totalCost }}</span></h5>' +
    '            <button class="form-control btn btn-outline-success text-dark ml-auto">Купить</button>' +
    '        </div>' +
    '    </div>' +
    '</footer>'
});