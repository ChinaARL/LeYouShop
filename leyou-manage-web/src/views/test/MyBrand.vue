<template>
    <div>
    <v-flex xs12 sm6>
        <v-text-field
                label="请输入查询条件"
                single-line
                v-model="key"
        ></v-text-field>
    </v-flex>
    <v-data-table
            :headers="headers"
            :items="brandList"
            :total-items="totalItems"
            :pagination.sync="pagination"
            class="elevation-1"
    >
        <template v-slot:items="props">
            <td class="text-xs-center">{{ props.item.id }}</td>
            <td class="text-xs-center">{{ props.item.name }}</td>
            <td class="text-xs-center">{{ props.item.letter }}</td>
            <img class="text-xs-center"><img :src="props.item.image"></img></td>
        </template>
    </v-data-table>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                headers: [
                    {
                        text: '编号',
                        align: 'center',
                        sortable: false,
                        value: 'id'
                    },
                    {text: '名称', align: 'center', value: 'name'},
                    {text: '首字母', align: 'center', value: 'letter'},
                    {text: '图片', align: 'center', value: 'logo'}
                ],
                brandList: [], //存放当前页品牌信息列表
                totalItems: 0, //总数据记录数
                pagination: {}, //分页信息,当前页码,页大小,排序字段,升序降序
                key:"", //查询关键字
            }
        },
        created() {
            this.getDataFromServer();
        },
        methods: {
            getDataFromServer() {
                //发送ajax请求获取数据,给brandList赋值
                //$http  ---   http://api.leyou.com/api
                this.$http.get("item/brand/page",
                    {
                        params: {
                            key: this.key,
                            page: this.pagination.page,
                            rows: this.pagination.rowsPerPage,
                            sortBy: this.pagination.sortBy,
                            desc: this.pagination.descending
                        }
                    }).then(resp => {
                    console.log(resp);
                    this.brandList = resp.data.items;
                    this.totalItems = resp.data.total;
                })
            }
        },
        watch: {
            "pagination": {
                deep: true,   //监控对象属性变换
                handler() {
                    console.log(this.pagination);
                    this.getDataFromServer();
                }
            },
            "key":{
                handler(){
                    this.getDataFromServer();
                }
            }
        }

    }
</script>

<style scoped>

</style>