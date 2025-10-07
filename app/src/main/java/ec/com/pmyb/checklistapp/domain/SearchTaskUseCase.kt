package ec.com.pmyb.checklistapp.domain

import ec.com.pmyb.checklistapp.data.repository.TaskRepository
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import javax.inject.Inject

class SearchTaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(text: String){
        repository.searchByText(text)
    }
}