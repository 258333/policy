<script setup>
import logo from '@/assets/LOGO.png'
import { Document, Tools } from '@element-plus/icons-vue'
</script>

<template>
    <el-container class="layout-container">
        <el-aside width="250px">
            <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
                <el-menu-item index="/">
                    <template #title>
                        <el-icon>
                            <Document />
                        </el-icon>
                        <span>科技文档管理</span>
                    </template>
                </el-menu-item>

                <el-sub-menu>
                    <template #title>
                        <el-icon>
                            <Tools />
                        </el-icon>
                        <span>系统设置</span>
                    </template>

                    <el-menu-item index="/set">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>设置</span>
                    </el-menu-item>
                </el-sub-menu>

            </el-menu>
        </el-aside>
        <el-container>
            <el-header>
                <div class="flex items-center">
                    <el-avatar :size="32" class="mr-3" :src="logo" style="margin-left: 290px;" />
                    <span class="text-sm mr-2" style="color: var(--el-text-color-regular);">
                        <strong style="font-size:220%; color:blue;">科技政策查询系统</strong>
                    </span>

                </div>
            </el-header>

            <el-main>
                <router-view></router-view>
            </el-main>

        </el-container>
    </el-container>
</template>

<style scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/xiongchumo.jpg') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>