package com.example.policy.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.policy.logic.creator.PolicyNetwork
import com.example.policy.logic.model.Results
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    //保存查询到的所有类别
    val typeList = mutableStateOf(emptyList<Map<String, Any>>())

    //搜索表单
    val form = mutableStateOf(SearchForm())

    //保存查询到的所有政策信息
    val policyList = mutableStateOf(emptyList<Map<String, Any>>())

    //查询所有的类别信息
    fun typeListFun() {
        viewModelScope.launch {
            try {
                val result: Results<List<Map<String, Any>>> = PolicyNetwork.typeListNet()
                typeList.value = result.data
                Log.d("Lhwtype", result.data.toString())
//                Log.d("Lhw",)
            } catch (e: Exception) {
                Log.e("Lhwtype", "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun onInputChange(str: String) {
        form.value = form.value.copy(
            input = str
        )
    }

    fun onCheckChange(str: String, isChecked: Boolean) {
        form.value = form.value.copy(
            checkList = if (isChecked) {
                form.value.checkList + str
            } else {
                form.value.checkList - str
            }
        )
        Log.d("lhw", form.value.checkList.toString())
    }
}

//搜索表单
data class SearchForm(
    val input: String = "",
    val checkList: List<String> = emptyList()
)