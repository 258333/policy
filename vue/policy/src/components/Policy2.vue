<script setup>
import { usePolicyStore } from '@/stores/policy';
import {ref} from 'vue'
const policyStore = usePolicyStore();
const policy = ref({ ...policyStore.policy })
</script>
<template>
    <div class="seeDetails">
        <div style="width: 500px; margin: 20px 0px 0px 70px">
            <p>l
            <div v-html="policy.text"></div>
            </p>
        </div>
    </div>
</template>
<style scoped>
.seeDetails {
    width: 1200px;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    align-items: center;

    div {
        span {
            padding-right: 15px;
            font-size: 14px;
            color: #8d91aa;
        }

        p {
            font-size: 14px;
            color: #2b2e30;
        }
    }
}
</style>