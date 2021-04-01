Vue.use(VueSession);

Vue.component('shopHeader', {
    template:
    '<header>' +
    '    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">' +
    '        <div class="d-flex flex-md-fill">' +
    '            <a class="navbar-brand" href="index.html">' +
    '                <img src="../static/images/beta_icon.png" width="60" height="40" alt="Логотипище...">' +
    '            </a>' +
    '            <a class="nav-link dropdown-toggle common-color" href="#" id="navbarDropdown" role="button"' +
    '               data-toggle="dropdown"' +
    '               aria-haspopup="true" aria-expanded="false">' +
    '                Категории' +
    '            </a>' +
    '            <div class="dropdown-menu" aria-labelledby="navbarDropdown">' +
    '                <a class="dropdown-item" href="#" v-for="category in categories">{{ category.categoryName }}</a>' +
    '            </div>' +
    '            <input class="form-control mr-sm-2" type="search" placeholder="Поиск" aria-label="Search">' +
    '            <img src="../static/images/search_icon.png" width="50" height="40" alt="Найти" class="btn btn-danger">' +
    '            <a href="cart.html">' +
    '                <img src="../static/images/bucket_icon.png" class="ml-3" width="50" height="40" alt="">' +
    '                <!--span class="common-color ml-1">Корзина</span-->' +
    '            </a>' +
    '            <a class="ml-4 common-color" v-on:click="onHeaderLabelPressed">' +
    '               {{ getHeaderLabel }}' +
    '            </a>' +
    '        </div>' +
    '    </nav>' +
    '</header>',
    data: function () {
        return {
            categories: []
        }
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
                window.location.href = 'cabinet.html?login=' + this.$session.get('login');
            else
                window.location.href = 'login.html';
        }
    },
    created: function () {
        if(this.categories.length > 0)
            this.categories.splice(0);
        this.$http.get('http://localhost:8080/api/utils/categories')
            .then((response) => {
                if(response.ok) {
                    response.json().then(data => data.forEach(category => this.categories.push(category)));
                }
            })
            .catch(error => {
                console.log(error.body.message);
            });
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
    '      <div class="form-group mt-2">' +
    '           <p> Количество: <select class="form-control" v-model="countOp">' +
    '               <option value=">" selected></option>' +
    '               <option value="<">&lt;</option>' +
    '               <option value=">=">&gt;=</option>' +
    '               <option value="<=">&lt;=</option>' +
    '               <option value="=">=</option>' +
    '           </select>' +
    '           <input type="number" class="form-control" v-model="count">' +
    '           </p>' +
    '      </div>' +
    '    </div>' +
    '    <button class="mt-3 btn btn-outline-secondary mx-auto" v-on="$emit(\'applyFilters\', shipType, ' +
    '                       condition, price, priceOp, count, countOp)">Применить</button>' +
    '  </div>' +
    '</div>',
    data: function () {
        return {
            shipType: true,
            condition: true,
            price: 0,
            priceOp: '>',
            count: 0,
            countOp: '>'
        }
    }
});

Vue.component('basicFooter', {
    props: ['page'],
    template: '<div>' +
    '<filters />' +
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
    '</footer>' +
    '</div>'
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

Vue.component('product', {
   props: ['product'],
    template: '<div class="modal fade" id="productModal" tabindex="-1" role="dialog" aria-hidden="true">' +
    '  <div class="modal-dialog modal-lg" role="document">' +
    '    <div class="modal-content">' +
    '      <div class="modal-header">' +
    '        <h5 class="modal-title" id="exampleModalLongTitle">Товар</h5>' +
    '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
    '          <span aria-hidden="true">&times;</span>' +
    '        </button>' +
    '      </div>' +
    '      <div class="modal-body" style="overflow-x: auto; overflow-y: auto;">' +
    '       <template v-if="product != null">' +
    '           <div class="d-flex mt-5">' +
    '               <div>' +
    '                   <img :src="product.photoURL" alt="Картинки нет..." class="img-fluid ml-3 mr-5"/>' +
    '               </div>' +
    '               <div class="form-group ml-5 mr-5">' +
    '                   <fieldset v-if="!product.editable">' +
    '                       <span class="text-warning" v-for="index in product.rating" v-on:click="rate(index)">&#9733;</span>' +
    '                       <!--span class="text-warning">&#9733;</span-->' +
    '                       <!--span class="text-warning">&#9733;</span-->' +
    '                       <span v-for="index in 5 - product.rating" v-on:click="rate(product.rating + index)">&#9734;</span>' +
    '                       <!--span>&#9734;</span-->' +
    '                   </fieldset>' +
    '                   <h3>Название</h3>' +
    '                   <input class="form-control w-50" placeholder="..." v-bind:value="product.productName" :readonly="!product.editable">' +
    '                   <p>Цена:</p>' +
    '                   <input type="number" class="form-control w-50 text-primary" placeholder="0" v-bind:value="product.price" :readonly="!product.editable">' +
    '                   <p>Доставка:</p>' +
    '                   <select class="form-control w-50" v-bind:value="product.shipType" :readonly="!product.editable">' +
    '                       <option value="true">За счет продавца</option>' +
    '                       <option value="false">За счет покупателя</option>' +
    '                   </select>' +
    '                   <p class="text-success mt-5" v-if="product.count > 0">В наличии:</p>' +
    '                   <p class="text-danger mt-5" v-else>В наличии:</p>' +
    '                   <input type="number" class="form-control w-50" placeholder="0" v-bind:value="product.count" :readonly="!product.editable">' +
    '                   <p class="mt-5">Продавец: <a :href="\'cabinet.html?login=\' + product.user.login">{{ product.user.login }}</a></p>' +
    '                   <p v-if="product.user.phone != null">Телефон: {{ product.user.phone }}</p>' +
    '                   <p v-if="product.user.email != null">E-mail: {{ product.user.email }}</p>' +
    '               </div>' +
    '               <div class="ml-auto mr-3" v-if="!product.editable">' +
    '                   <div class="card text-center border border-secondary">' +
    '                       <div class="card-header">' +
    '                           <input type="number" class="form-control mb-2 text-center" placeholder="1">' +
    '                               <button class="form-control btn btn-outline-success text-dark">В корзину</button>' +
    '                       </div>' +
    '                       <div class="card-footer">' +
    '                           <button class="form-control btn btn-outline-warning text-dark">В желаемое</button>' +
    '                       </div>' +
    '                   </div>' +
    '               </div>' +
    '           </div>' +
    '           <div class="ml-2">' +
    '               <button v-if="product.editable" class="btn btn-outline-secondary mt-2">Сохранить изменения</button>' +
    '               <datalist id="producers">' +
    '                   <option v-for="producer in producers">{{ producer.producerName }}</option>' +
    '               </datalist>' +
    '               <datalist id="categories">' +
    '                   <option v-for="category in categories">{{ category.categoryName }}</option>' +
    '               </datalist>' +
    '               <h6 class="mt-4">Производитель:</h6>' +
    '               <input class="form-control w-50" placeholder="..." list="producers" v-bind:value="product.producer.producerName" :readonly="!product.editable">' +
    '               <h6 class="mt-3">Состояние:</h6>' +
    '               <select class="form-control w-50" v-bind:value="product.condition" :readonly="!product.editable">' +
    '                   <option value="true">Новое</option>' +
    '                   <option value="false">Б/у</option>' +
    '               </select>' +
    '               <h6 class="mt-3">Категория:</h6>' +
    '               <input class="form-control w-50" placeholder="..." list="categories" v-bind:value="product.category.categoryName" :readonly="!product.editable">' +
    '               <h6 class="mt-3">Краткое описание:</h6>' +
    '               <textarea class="form-control w-50" :readonly="!product.editable">' +
    '                   {{ product.description }}' +
    '               </textarea>' +
    '           </div>' +
    '        </template>' +
    '       </div>' +
    '   </div>' +
    ' </div>' +
    '</div>',
    data: function () {
        return {
            producers: [],
            categories: []
        }
    },
    methods: {
      rate: function (new_rating) {
          this.$http.post('http://localhost:8080/api/products/get/' + this.product.id + '/rate/' + new_rating)
              .then(response => {
              if(response.ok) {
                this.product.rating = new_rating;
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
        if(this.producers.length > 0)
            this.producers.splice(0);
        this.$http.get('http://localhost:8080/api/utils/producers')
            .then((response) => {
                if(response.ok) {
                    response.json().then(data => data.forEach(producer => this.producers.push(producer)));
            }
        })
            .catch(error => {
                console.log(error.body.message);
        });
        if(this.categories.length > 0)
            this.categories.splice(0);
        this.$http.get('http://localhost:8080/api/utils/categories')
            .then((response) => {
                if(response.ok) {
                    response.json().then(data => data.forEach(category => this.categories.push(category)));
            }
        })
            .catch(error => {
                console.log(error.body.message);
        });
    }
});

Vue.component('addProduct', {
   template: '<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">' +
   '  <div class="modal-dialog modal-lg" role="document">' +
   '    <div class="modal-content">' +
   '      <div class="modal-header">' +
   '        <h5 class="modal-title" id="exampleModalLongTitle">Новый товар</h5>' +
   '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
   '          <span aria-hidden="true">&times;</span>' +
   '        </button>' +
   '      </div>' +
   '      <div class="modal-body" style="overflow-x: auto; overflow-y: auto;">' +
   '            <div class="form-group"><p class="lead">Название:</p>' +
   '               <input class="form-control w-50" v-bind:value="productName"></div>' +
   '            <div class="form-group mt-4"><p class="lead">Категория:</p>' +
   '               <datalist id="categories">' +
   '                   <option v-for="category in categories">{{ category.categoryName }}</option>' +
   '               </datalist>' +
   '               <input class="form-control w-50" list="categories" v-bind:value="categoryName"></div>' +
   '            <div class="form-group mt-4"><p class="lead">Фото:</p>' +
   '               <input type="file" accept="image/jpeg,image/jpg,image/png,image/gif" v-bind:value="photo"></div>' +
   '            <div class="form-group mt-4"><p class="lead">Производитель:</p>' +
   '               <datalist id="producers">' +
   '                   <option v-for="producer in producers">{{ producer.producerName }}</option>' +
   '               </datalist>' +
   '               <input class="form-control w-50" list="producers" v-bind:value="producerName"></div>' +
   '            <div class="form-group mt-4"><p class="lead">Цена:</p>' +
   '               <input type="number" min="0" class="form-control w-50" v-bind:value="price"></div>' +
   '            <div class="form-group mt-4"><p class="lead">Состояние:</p>' +
   '               <select class="form-control w-50" v-bind:value="condition">' +
   '                   <option value="true">Новое</option>' +
   '                   <option value="false">Б/у</option>' +
   '               </select></div>' +
   '            <div class="form-group mt-4"><p class="lead">Доставка:</p>' +
   '                   <select class="form-control w-50" v-bind:value="shipType">' +
   '                       <option value="true">За счет продавца</option>' +
   '                       <option value="false">За счет покупателя</option>' +
   '                   </select></div>' +
   '            <div class="form-group mt-4"><p class="lead">Количество:</p>' +
   '               <input type="number" min="0" class="form-control w-50" v-bind:value="count"></div>' +
   '            <div class="form-group mt-4"><p class="lead">Описание:</p>' +
   '               <textarea class="form-control w-50" v-bind:value="description"></textarea></div>' +
   '      </div>' +
   '      <div class="modal-footer">' +
   '        <button class="btn btn-outline-success" v-on:click="add">Добавить</button>' +
   '      </div>' +
   '    </div>' +
   '  </div>' +
   '</div>',
   data: function () {
       return {
          categories: [],
          producers: [],
          productName: null,
          categoryName: null,
          photo: null,
          producerName: null,
          price: 0,
          condition: true,
          shipType: true,
          count: 0,
          description: null
       }
   },
   methods: {
       add: function () {

       }
   },
    created: function () {
        this.$http.get('http://localhost:8080/api/utils/producers')
            .then((response) => {
            if(response.ok) {
            response.json().then(data => data.forEach(producer => this.producers.push(producer)));
        }
    })
    .catch(error => {
            console.log(error.body.message);
    });
        if(this.categories.length > 0)
            this.categories.splice(0);
        this.$http.get('http://localhost:8080/api/utils/categories')
            .then((response) => {
            if(response.ok) {
            response.json().then(data => data.forEach(category => this.categories.push(category)));
        }
    })
    .catch(error => {
            console.log(error.body.message);
    });
    }
});

Vue.component('changePass', {
    props: ['id'],
    template: '<div class="modal fade" id="changePassModal" tabindex="-1" role="dialog" aria-hidden="true">' +
    '  <div class="modal-dialog" role="document">' +
    '    <div class="modal-content">' +
    '      <div class="modal-header">' +
    '        <h5 class="modal-title" id="exampleModalLabel">Смена пароля</h5>' +
    '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
    '          <span aria-hidden="true">&times;</span>' +
    '        </button>' +
    '      </div>' +
    '      <div class="modal-body" style="overflow-y: auto;">' +
    '        <div class="form-group"><p class="lead">Новый пароль:</p>' +
    '        <input class="form-control w-50" v-bind:value="pass"></div>' +
    '        <div class="form-group mt-4"><p class="lead">Подтверждение пароля:</p>' +
    '        <input class="form-control w-50" v-bind:value="confirm"></div>' +
    '      </div>' +
    '      <div class="modal-footer">' +
    '        <button type="button" class="btn btn-outline-secondary" v-on:click="change">Подтвердить</button>' +
    '      </div>' +
    '    </div>' +
    '  </div>' +
    '</div>',
    data: function () {
        return {
            pass: '',
            confirm: ''
        }
    },
    methods: {
        change: function () {

        }
    }
});

Vue.component('deleteAcc', {
    props: ['text'],
    template: '<div class="modal fade" id="delAccModal" tabindex="-1" role="dialog" aria-hidden="true">' +
    '  <div class="modal-dialog" role="document">' +
    '    <div class="modal-content">' +
    '      <div class="modal-header">' +
    '        <h5 class="modal-title" id="exampleModalLabel">Подтвердите удаление</h5>' +
    '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
    '          <span aria-hidden="true">&times;</span>' +
    '        </button>' +
    '      </div>' +
    '      <div class="modal-body">' +
    '        {{ text }}' +
    '      </div>' +
    '      <div class="modal-footer">' +
    '        <button type="button" class="btn btn-outline-danger" v-on:click="$emit(\'deleteAnyway\')">Удалить</button>' +
    '      </div>' +
    '    </div>' +
    '  </div>' +
    '</div>'
});