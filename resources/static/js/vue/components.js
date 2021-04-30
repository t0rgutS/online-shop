Vue.use(VueSession);

Vue.component('errorAlert', {
    props: ['error'],
    template:
    '<div v-if="error != null" id="error" class="alert alert-danger alert-dismissible fade show mt-2 mb-2" role="alert" aria-hidden="true">' +
    '<span> {{ error }} </span>' +
    '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
    '<span aria-hidden="true">&times;</span>' +
    '</button>' +
    '</div>'
});

Vue.component('successAlert', {
    props: ['text'],
    template:
    '<div id="success" role="alert" class="alert alert-success alert-dismissible fade show mt-2 mb-2">' +
    '<span> {{ text }} </span>' +
    '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
    '<span aria-hidden="true">&times;</span>' +
    '</button>' +
    '</div>'
});

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
    '                <a class="dropdown-item" href="#" v-on:click="$emit(\'categorySet\', null)">Все</a>' +
    '                <a class="dropdown-item" href="#" v-for="category in categories" ' +
    '                              v-on:click="$emit(\'categorySet\', category.categoryName)">{{ category.categoryName }}</a>' +
    '            </div>' +
    '            <input class="form-control mr-sm-2" type="search" placeholder="Поиск" aria-label="Search" v-model="searchString">' +
    '            <img src="../static/images/search_icon.png" width="50" height="40" alt="Найти" class="btn btn-danger" ' +
    '                              v-on:click="$emit(\'searchSet\', searchString)">' +
    '            <a href="cart.html">' +
    '                <img src="../static/images/bucket_icon.png" class="ml-3" width="50" height="40" alt="">' +
    '                <!--span class="common-color ml-1">Корзина</span-->' +
    '            </a>' +
    '            <a href="#" class="ml-4 mt-2" v-on:click="onHeaderLabelPressed">' +
    '               <span class="common-color">{{ getHeaderLabel }}</span>' +
    '            </a>' +
    '        </div>' +
    '    </nav>' +
    '</header>',
    data: function () {
        return {
            categories: [],
            searchString: null
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
                alert("Ошибка: " + error.body.message);
            });
    }
});

Vue.component('filters', {
    template: '<div class="modal fade bd-example-modal-sm" id="filterModal" tabindex="-1" role="dialog" ' +
    'aria-hidden="true">' +
    '  <div class="modal-dialog modal-sm">' +
    '    <div class="modal-content">' +
    '       <div class="modal-header">' +
    '           <h5 class="modal-title">Фильтры</h5>' +
    '           <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
    '               <span aria-hidden="true">&times;</span>' +
    '           </button>' +
    '       </div>' +
    '       <div class="modal-body">' +
    '      <div class="form-inline">' +
    '           <p> Доставка: <select class="form-control-sm" v-model="shipType">' +
    '               <option value="null">Не выбрано</option>' +
    '               <option value="true">За счет продавца</option>' +
    '               <option value="false">За счет покупателя</option>' +
    '           </select></p>' +
    '      </div>' +
    '      <div class="form-inline mt-2">' +
    '           <p> Состояние: <select class="form-control-sm" v-model="condition">' +
    '               <option value="null">Не выбрано</option>' +
    '               <option value="true">Новое</option>' +
    '               <option value="false">Б/у</option>' +
    '           </select></p>' +
    '      </div>' +
    '      <div class="form-inline mt-2">' +
    '           <p> Цена: <select class="form-control-sm" v-model="priceOp">' +
    '               <option value=">">&gt;</option>' +
    '               <option value="<">&lt;</option>' +
    '               <option value=">=">&gt;=</option>' +
    '               <option value="<=">&lt;=</option>' +
    '               <option value="=">=</option>' +
    '           </select>' +
    '           <input type="number" min="0" step="0.01" class="form-control-sm w-25" v-model="price">' +
    '           </p>' +
    '      </div>' +
    '      <div class="form-inline mt-2">' +
    '           <p> Количество: <select class="form-control-sm" v-model="countOp">' +
    '               <option value=">">&gt;</option>' +
    '               <option value="<">&lt;</option>' +
    '               <option value=">=">&gt;=</option>' +
    '               <option value="<=">&lt;=</option>' +
    '               <option value="=">=</option>' +
    '           </select>' +
    '           <input type="number" min="0" class="form-control-sm w-25" v-model="count">' +
    '           </p>' +
    '      </div>' +
    '       </div>' +
    '    <div class="modal-footer">' +
    '       <button class="mt-3 btn btn-outline-success" v-on:click="$emit(\'applyFilters\', shipType, ' +
    '                       condition, price, priceOp, count, countOp)">Применить</button>' +
    '       <button class="mt-3 btn btn-outline-danger" v-on:click="reset">Сбросить</button>' +
    '    </div>' +
    '    </div>' +
    '  </div>' +
    '</div>',
    data: function () {
        return {
            shipType: null,
            condition: null,
            price: 0,
            priceOp: '>',
            count: 0,
            countOp: '>'
        }
    },
    methods: {
        reset: function () {
            this.shipType = null;
            this.condition = null;
            this.price = 0;
            this.priceOp = '>';
            this.count = 0;
            this.countOp = '>';
            this.$emit('applyFilters', this.shipType, this.condition, this.price, this.priceOp, this.count, this.countOp);
        }
    }
});

Vue.component('basicFooter', {
    props: ['page'],
    template: '<div>' +
    '<filters v-on:applyFilters="forwardFilters" />' +
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
    '</div>',
    methods: {
        forwardFilters: function (shipType, condition, price, priceOp, count, countOp) {
            this.$emit('applyFilters', shipType, condition, price, priceOp, count, countOp);
        }
    }
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
    '       <errorAlert :error="error" />' +
    '       <successAlert :text="text"' +
    '       <template v-if="product != null">' +
    '           <div class="d-flex mt-5">' +
    '               <div>' +
    '                   <input type="file" ref="newImage" style="display: none" v-on:change="photo=$refs.newImage.value">' +
    '                   <img :src="product.photoURL" alt="Картинки нет..." class="img-fluid ml-3 mr-5" v-on:click="$refs.newImage.click()" />' +
    '               </div>' +
    '               <div class="form-group ml-5 mr-5">' +
    '                   <fieldset>' +
    '                       <span class="text-warning" v-for="index in product.rating" v-on:click="rate(index)">&#9733;</span>' +
    '                       <!--span class="text-warning">&#9733;</span-->' +
    '                       <!--span class="text-warning">&#9733;</span-->' +
    '                       <span v-for="index in 5 - product.rating" v-on:click="rate(product.rating + index)">&#9734;</span>' +
    '                       <span v-if="product.rating > 0 && !product.editable" class="ml-2 text-primary" v-on:click="derate"><small>Сбросить</small></span>' +
    '                       <!--span>&#9734;</span-->' +
    '                   </fieldset>' +
    '                   <h3>Название</h3>' +
    '                   <input class="form-control w-50" placeholder="..." v-bind:value="productName" :readonly="!product.editable">' +
    '                   <p>Цена:</p>' +
    '                   <input type="number" class="form-control w-50 text-primary" placeholder="0" v-bind:value="price" :readonly="!product.editable">' +
    '                   <p>Доставка:</p>' +
    '                   <select class="form-control w-50" v-bind:value="shipType" :readonly="!product.editable">' +
    '                       <option value="true">За счет продавца</option>' +
    '                       <option value="false">За счет покупателя</option>' +
    '                   </select>' +
    '                   <p class="text-success mt-5" v-if="product.count > 0">В наличии:</p>' +
    '                   <p class="text-danger mt-5" v-else>В наличии:</p>' +
    '                   <input type="number" class="form-control w-50" placeholder="0" v-bind:value="count" :readonly="!product.editable">' +
    '                   <p class="mt-5">Продавец: <a :href="\'cabinet.html?login=\' + product.user.login">{{ product.user.login }}</a></p>' +
    '                   <p v-if="product.user.phone != null">Телефон: {{ product.user.phone }}</p>' +
    '                   <p v-if="product.user.email != null">E-mail: {{ product.user.email }}</p>' +
    '               </div>' +
    '               <div class="ml-auto mr-3" v-if="!product.editable">' +
    '                   <div class="card text-center border border-secondary">' +
    '                       <div class="card-header">' +
    '                           <input type="number" class="form-control mb-2 text-center" min="1" v-bind:value="cartCount" :readonly="canAddToCart">' +
    '                               <button class="form-control btn btn-outline-success text-dark" v-on:click="toCart" :disabled="canAddToCart">В корзину</button>' +
    '                       </div>' +
    '                       <div class="card-footer">' +
    '                           <button class="form-control btn btn-outline-warning text-dark" v-on:click="toWishList" :disabled="canAddToWishList">В желаемое</button>' +
    '                       </div>' +
    '                   </div>' +
    '               </div>' +
    '           </div>' +
    '           <div class="ml-2">' +
    '               <button v-if="product.editable" class="btn btn-outline-secondary mt-2" v-on:click="saveChanges">Сохранить изменения</button>' +
    '               <datalist id="producers">' +
    '                   <option v-for="producer in producers">{{ producer.producerName }}</option>' +
    '               </datalist>' +
    '               <datalist id="categories">' +
    '                   <option v-for="category in categories">{{ category.categoryName }}</option>' +
    '               </datalist>' +
    '               <h6 class="mt-4">Производитель:</h6>' +
    '               <input class="form-control w-50" placeholder="..." list="producers" v-bind:value="producer" :readonly="!product.editable">' +
    '               <h6 class="mt-3">Состояние:</h6>' +
    '               <select class="form-control w-50" v-bind:value="condition" :readonly="!product.editable">' +
    '                   <option value="true">Новое</option>' +
    '                   <option value="false">Б/у</option>' +
    '               </select>' +
    '               <h6 class="mt-3">Категория:</h6>' +
    '               <input class="form-control w-50" placeholder="..." list="categories" v-bind:value="categoryName" :readonly="!product.editable">' +
    '               <h6 class="mt-3">Краткое описание:</h6>' +
    '               <textarea class="form-control w-50" :readonly="!product.editable">' +
    '                   {{ description }}' +
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
            categories: [],
            productName: null,
            photo: null,
            producer: null,
            price: null,
            description: null,
            condition: null,
            shipType: null,
            count: null,
            categoryName: null,
            cartCount: 1,
            error: null,
            text: '',
            canAddToCart: false,
            canAddToWishList: false
        }
    },
    watch: {
      product: function () {
          this.productName = this.product.productName;
          this.photo = null;
          this.producer = this.product.producer.producerName;
          this.price = this.product.price;
          this.description = this.product.description;
          this.condition = this.product.condition;
          this.shipType = this.product.shipType;
          this.count = this.product.count;
          this.categoryName = this.product.category.categoryName;
          this.cartCount = 1;
          this.error = null;
      }
    },
    methods: {
      rate: function (new_rating) {
          if(!this.product.editable)
          this.$http.post('http://localhost:8080/api/products/get/' + this.product.id + '/rate/' + new_rating, {
              headers: {
                  Authorization: 'Bearer ' + this.$session.get('token')
              }
          })
              .then(response => {
              if(response.ok) {
                this.product.rating = new_rating;
              }
          })
            .catch(error => {
                if(error.status == 403)
                    window.location.href = 'login.html';
                alert("Ошибка: " + error.body.message);
          });
      },
      derate: function () {
          if(!this.product.editable)
          this.$http.post('http://localhost:8080/api/products/get/' + this.product.id + '/derate', {
              headers: {
                  Authorization: 'Bearer ' + this.$session.get('token')
              }
          })
              .then(response => {
              if(response.ok) {
                this.product.rating = new_rating;
              }
        })
            .catch(error => {
              if(error.status == 403)
                window.location.href = 'login.html';
              alert("Ошибка: " + error.body.message);
          });
      },
      toCart: function () {
          this.$http.post('http://localhost:8080/api/cartwish/create', JSON.stringify({
                    productId: this.product.id,
                    count: this.cartCount
                }),  {
                headers: {
                  Authorization: 'Bearer ' + this.$session.get('token')
                }
            }).then(response => {
              if(response.ok) {
                alert("Товар успешно добавлен в корзину!");
              }
          }).catch();
      },
      toWishList: function () {
          this.$http.post('http://localhost:8080/api/cartwish/create', JSON.stringify({
                  productId: this.product.id,
                  count: 0
              }), {
              headers: {
                    Authorization: 'Bearer ' + this.$session.get('token')
              }
          }).then(response => {
              if(response.ok) {
                alert("Товар успешно добавлен в желаемое!");
              }
          }).catch();
      },
      canAddTo: function () {
          if(this.product != null) {
            this.$http.get('http://localhost:8080/api/cartwish/check/cart/' + this.product.id, {
                  headers: {
                      Authorization: 'Bearer ' + this.$session.get('token')
                  }
              }).then(response => {
                  this.canAddToCart = response.body;
            }).catch(error => console.log(error.body.message));
            this.$http.get('http://localhost:8080/api/cartwish/check/wishlist/' + this.product.id, {
                  headers: {
                      Authorization: 'Bearer ' + this.$session.get('token')
                  }
              }).then(response => {
                  this.canAddToWishList = response.body;
            }).catch(error => console.log(error.body.message));
          }
      },
      saveChanges: function () {
          this.$http.put('http://localhost:8080/api/products/update/' + this.product.id, {
              body: {
                productName: this.productName,
                photo: this.photo,
                producer: this.producer,
                price: this.price,
                description: this.description,
                condition: this.condition,
                shipType: this.shipType,
                count: this.count,
                categoryName: this.categoryName
              }, headers: {
                  Authorization: 'Bearer ' + this.$session.get('token')
              }
          }).then(response => {
              if(response.ok) {
                response.json().then(data => this.$emit('update', product, data));
              } else {
                alert("Неизвестная ошибка!");
              }
          }).catch(error => {
              alert("Ошибка: " + error.body.message);
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
        this.canAddTo();
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
   '            <errorAlert :error="error" />' +
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
          description: null,
          error: ''
       }
   },
   methods: {
       add: function () {
           this.$http.post('http://localhost:8080/api/products/create', {
               body: {
                   productName: this.productName,
                   photo: this.photo,
                   producer: this.productName,
                   price: this.price,
                   description: this.description,
                   condition: this.condition,
                   shipType: this.shipType,
                   count: this.count,
                   categoryName: this.categoryName
               }, headers: {
                   Authorization: 'Bearer ' + this.$session.get('token')
               }
           }).then(response => {
               if(response.ok)  {
                   response.json().then(data => this.$emit('insert', data));
                   $('addModal').modal('hide');
                } else {
                    alert("Неизвестная ошибка!");
                }
            }
           ).catch(error => {
               alert("Ошибка: " + error.body.message);
           });
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

Vue.component('cartComponent', {
    props: ['cartwish'],
    template: '<template>' +
    ' <div class="col-sm-8 col-lg-4">' +
    '   <div class="card mt-3 mb-3">' +
    '       <div class="card-header text-center">' +
    '           <button type="button" class="btn btn-outline-danger close" aria-label="Close" v-on:click="this.$emit(\'remove\', cartwish)">' +
    '               <span aria-hidden="true">&times;</span>' +
    '           </button>' +
    '           <h5 class="card-title">{{ cartwish.product.productName }}</h5>' +
    '       </div>' +
    '       <div class="card-body text-center">' +
    '           <img class="card-img-top img-fluid"' +
    '               v-bind:src="cartwish.product.photoURL" alt="Картинки нет...">' +
    '       </div>' +
    '       <div class="card-footer">' +
    '           <p class="card-text text-primary">{{ cartwish.product.price }}</p>' +
    '           <div class="form-group mt-3">' +
    '               <input type="number" class="form-control mb-2 text-center mx-auto" ' +
    '                     placeholder="1" min="1" v-bind:value="count" v-on:change="changeCount">' +
    '               <button class="form-control btn btn-outline-success text-dark" v-on:click="open(cartwish.product)">' +
    '                    Открыть' +
    '               </button>' +
    '           </div>' +
    '       </div>' +
    '   </div>' +
    ' </div>' +
    '</template>',
    data: function () {
        return {
            count: this.cartwish.count
        }
    },
    methods: {
        changeCount: function () {
            this.$http.put('http://localhost:8080/api/cartwish/update/' + this.cartwish.id, {
                body: {
                    productId: this.cartwish.product.id,
                    count: this.count
                }, headers: {
                    Authorization: 'Bearer ' + this.$session.get('token')
                }
            }).then(response => {
                if(response.ok) {
                    response.json().then(data => this.$emit('update', cartwish, data));
                } else this.$emit('error', 'Неизвестная ошибка!');
            }).catch(error => {
                this.$emit('error', error.body.message);
            })
        }
    }
})

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
    '        <errorAlert :error="error" />' +
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
            confirm: '',
            error: null
        }
    },
    methods: {
        change: function () {
            if(this.pass == '') {
                this.error = 'Укажите новый пароль!';
                $('#error').alert('show');
            } else if(this.pass != this.confirm) {
                this.error = 'Пароль и подтверждение не совпадают!';
                $('#error').alert('show');
            } else {
                this.$http.put('http://localhost:8080/api/users/changepass', {
                    body: {
                        newPassword: this.pass
                    },
                    headers: {
                        Authorization: 'Bearer ' + this.$session.get('token')
                    }
                }).then(response => {
                    if(response.ok) {
                        response.json().then(data => this.$emit('update', data));
                    } else {
                        alert("Неизвестная ошибка!");
                    }
                }).catch(error => {
                    alert("Ошибка: " + error.body.message);
                });
            }
        }
    }
});

Vue.component('deleteObj', {
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