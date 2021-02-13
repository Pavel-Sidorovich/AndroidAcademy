package com.pavesid.androidacademy.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.TestData
import com.pavesid.androidacademy.data.details.DetailsWithCredits
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val coroutineDispatcher = TestCoroutineDispatcher()

    private var repository: MoviesRepository = mockk()

    private var detailsWithCreditsObserverMockito: Observer<Resource<DetailsWithCredits>> =
        mockk(relaxUnitFun = true)

    private lateinit var viewModel: DetailsViewModel

    @Before
    fun before() {
        viewModel = DetailsViewModel(repository, coroutineDispatcher)
        viewModel.detailsWithCredits.observeForever(detailsWithCreditsObserverMockito)
    }

    @After
    fun clear() {
        viewModel.detailsWithCredits.removeObserver(detailsWithCreditsObserverMockito)
    }

    @Test
    fun `should return response is success when repository return success details`() {
        coEvery { repository.getDetails(any()) } returns TestData.getDetails()

        viewModel.loadDetails(1)

        verifyOrder {
            detailsWithCreditsObserverMockito.onChanged(Resource.loading())
            detailsWithCreditsObserverMockito.onChanged(Resource.success(TestData.getDetails()))
        }
    }

    @Test
    fun `should return response is error when repository return error details`() {
        val message = "error"
        coEvery { repository.getDetails(any()) } throws RuntimeException(message)

        viewModel.loadDetails(1)

        verifyOrder {
            detailsWithCreditsObserverMockito.onChanged(Resource.loading())
            detailsWithCreditsObserverMockito.onChanged(Resource.error(message))
        }
    }
}
