package com.example.policy.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.policy.logic.creator.PolicyNetwork
import com.example.policy.logic.model.Results
import com.example.policy.logic.model.Type
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    //保存查询到的所有类别
    val typeList = mutableStateOf(emptyList<Type>())

    //搜索表单
    val form = mutableStateOf(SearchForm())

    //查询所有的类别信息
    fun typeListFun() {
        viewModelScope.launch {
            try {
                val result: Results<List<Type>> = PolicyNetwork.typeListNet()
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

}

//搜索表单
data class SearchForm(
    val input: String = "",
    val name: String? = null,
    val document: String? = null,
    val organ: String? = null,
    val text: String? = null
)