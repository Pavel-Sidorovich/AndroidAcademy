package com.pavesid.androidacademy.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.TestHelper
import com.pavesid.androidacademy.data.details.DetailsWithCredits
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
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

    @MockK
    lateinit var repository: MoviesRepository

    @MockK
    lateinit var detailsWithCreditsObserverMockito: Observer<Resource<DetailsWithCredits>>

    private lateinit var viewModel: DetailsViewModel

    private val message = "error"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        every { detailsWithCreditsObserverMockito.onChanged(any()) } answers {}
        viewModel = DetailsViewModel(repository, coroutineDispatcher)
        viewModel.detailsWithCredits.observeForever(detailsWithCreditsObserverMockito)
    }

    @After
    fun clear() {
        viewModel.detailsWithCredits.removeObserver(detailsWithCreditsObserverMockito)
    }

    @Test
    fun `should return response is success when repository return success details`() {
        coEvery { repository.getDetails(any()) } returns TestHelper.getDetails()

        viewModel.loadDetails(1)

        verifyOrder {
            detailsWithCreditsObserverMockito.onChanged(Resource.loading())
            detailsWithCreditsObserverMockito.onChanged(Resource.success(TestHelper.getDetails()))
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
