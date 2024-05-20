<script setup>
import { ref, onBeforeMount } from 'vue'
import { policyListService, typeListService } from '@/api/policy';
import { usePolicyStore } from '@/stores/policy';
import { useRouter } from 'vue-router'

//复选框中展示所有政策类型
let checks = ref([
    {
        type: '综合',
        num: 2,
    },
    {
        type: '科研机构改革',
        num: 4,
    },
])
//查询所有政策类型
const typeList = async () => {
    let result = await typeListService();
    checks.value = result.data.data;
}
typeList();

//复选框模型
const checkList = ref([])

//政策模型
let polices = ref([
    {
        name: '中共中央国务院关于完整准确全面贯彻新发展理念做好 碳达峰碳中和工作的意见',
        type: '综合',
        organ: '国务院',
        viadata: '2021-09-22',
    },
    {
        name: '中共中央国务院关于完整准确全面贯彻新发展理念做好 碳达峰碳中和工作的意见',
        type: '综合',
        organ: '国务院',
        viadata: '2021-09-22',
    },
    {
        name: '中共中央国务院关于完整准确全面贯彻新发展理念做好 碳达峰碳中和工作的意见',
        type: '综合',
        organ: '国务院',
        viadata: '2021-09-22',
    },
    {
        name: '中共中央国务院关于完整准确全面贯彻新发展理念做好 碳达峰碳中和工作的意见',
        type: '综合',
        organ: '国务院',
        viadata: '2021-09-22',
    },
])

//搜索表单模型
let search = ref({
    name: null,
    document: null,
    organ: null,
    text: null
})

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(10)//每页条数

// var encodedList = checkList.value.map(item => encodeURIComponent(item));  // 对每个元素进行 URL 编码
// var encodedListString = encodedList.join('&');  // 将编码后的元素拼接成字符串，用 & 分隔

//查询所有政策
const policyList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        name: search.value.name,
        document: search.value.document,
        organ: search.value.organ,
        text: search.value.text,
        checkList: checkList.value
    }
    // console.log(checkList.value)
    let result = await policyListService(params);
    polices.value = result.data.data.items;
    total.value = result.data.data.total;
}
policyList();

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    policyList();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    policyList();
}
const policyStore = usePolicyStore();
const router = useRouter()
const chakan = (row) => {
    console.log(row)
    policyStore.setPolicy(row)
    router.push("/policy2");
}

</script>

<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-row :gutter="30">
                    <el-col :span="4">
                        <span>图解政策</span>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item label="政策名称">
                            <el-input v-model="search.name" placeholder="请输入政策名称" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item label="发文字号">
                            <el-input v-model="search.document" placeholder="请输入发文字号" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item label="发文机构">
                            <el-input v-model="search.organ" clearable />
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item label="全文检索">
                            <el-input v-model="search.text" clearable />
                        </el-form-item>
                    </el-col>
                    <el-row>
                        <el-button type="primary" @click="policyList">搜索</el-button>
                    </el-row>
                </el-row>
            </el-header>
            <el-container>
                <el-aside width="240px">
                    <el-checkbox-group v-model="checkList">
                        <div v-for="(check, index) in checks" :key="index">
                            <div v-if="check.type == null">
                                <el-checkbox label="无类型" value="null" />
                                <el-text>({{ check.num }})</el-text>
                            </div>
                            <div v-else>
                                <el-checkbox :label="check.type" :value="check.type" />
                                <el-text>({{ check.num }})</el-text>
                            </div>

                        </div>
                        <!-- <el-button @click="console.log(checkList)"></el-button> -->
                    </el-checkbox-group>
                </el-aside>
                <el-main>
                    <el-table :data="polices" stripe style="width: 1000px">
                        <el-table-column prop="name" label="政策名称" width="180" show-overflow-tooltip />
                        <el-table-column prop="organ" label="发文机构" width="180" show-overflow-tooltip />
                        <el-table-column prop="viadata" label="颁布日期" width="180" show-overflow-tooltip />
                        <el-table-column prop="type" label="政策分类" width="180" show-overflow-tooltip />
                        <el-table-column label="操作" width="">
                            <template #default="{ row }">
                                <el-button plain type="primary" @click="chakan(row)">查看</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <!-- 分页条 -->
                    <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize"
                        :page-sizes="[5, 10, 15, 20]" layout="jumper, total, sizes, prev, pager, next" background
                        :total="total" @size-change="onSizeChange" @current-change="onCurrentChange"
                        style="margin-top: 20px;margin-left: 300px;" />
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>