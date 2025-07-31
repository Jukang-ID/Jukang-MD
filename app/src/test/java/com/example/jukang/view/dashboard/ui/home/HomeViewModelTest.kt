package com.example.jukang.view.dashboard.ui.home


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.jukang.data.response.Dokumen
import com.example.jukang.data.response.Lokasi
import com.example.jukang.data.response.TukangListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: HomeRepository

    @Mock
    private lateinit var tukangObserver: Observer<List<TukangListItem>?>

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel()
        viewModel.repository = repository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test fetchTukang ketika sukses harus mengembalikan data`() = testDispatcher.runBlockingTest {

        val dummyTukangList = generateDummyTukangList()
        val domisili = "Pontianak"

        whenever(repository.getMainData(domisili)).thenReturn(dummyTukangList)

        viewModel.dataTukang.observeForever(tukangObserver)
        viewModel.loadingHome.observeForever(loadingObserver)

        viewModel.fetchTukang(domisili)

        verify(loadingObserver).onChanged(true)

        verify(tukangObserver).onChanged(dummyTukangList)

        verify(loadingObserver).onChanged(false)

        viewModel.dataTukang.removeObserver(tukangObserver)
        viewModel.loadingHome.removeObserver(loadingObserver)
    }

    @Mock
    private lateinit var isEmpetyObserver: Observer<Boolean>

    @Test
    fun `test fetchTukang ketika data kosong harus set isEmpety menjadi true`() = testDispatcher.runBlockingTest {
        val emptyTukangList = emptyList<TukangListItem>()
        val domisili = "Pontianak"

        whenever(repository.getMainData(domisili)).thenReturn(emptyTukangList)

        viewModel.isEmpety.observeForever(isEmpetyObserver)
        viewModel.loadingHome.observeForever(loadingObserver)

        viewModel.fetchTukang(domisili)

        verify(loadingObserver).onChanged(true)

        verify(isEmpetyObserver).onChanged(true)

        verify(loadingObserver).onChanged(false)

        verify(tukangObserver, org.mockito.Mockito.never()).onChanged(org.mockito.kotlin.any())

        viewModel.isEmpety.removeObserver(isEmpetyObserver)
        viewModel.loadingHome.removeObserver(loadingObserver)
    }
}


fun generateDummyTukangList(): List<TukangListItem> {
    val tukang1 = TukangListItem(
        tukangId = "TKG-001",
        namatukang = "Udin Sedunia",
        spesialis = "Ahli Listrik & AC",
        domisili = "Pontianak",
        price = 150000,
        priceRupiah = "Rp 150.000",
        photoUrl = "https://example.com/udin.jpg",
        reviewCount = 25,
        totalReviewRating = 4.8,
        booked = false,
        nomorTelpon = "081234567890",
        dokumen = Dokumen(ktp = "ada", sim = "ada", npwp = null),
        lokasi = Lokasi(lat = -0.0273, lon = 109.3496)
    )

    val tukang2 = TukangListItem(
        tukangId = "TKG-002",
        namatukang = "Bambang Perkasa",
        spesialis = "Spesialis Pipa Air",
        domisili = "Pontianak",
        price = 120000,
        priceRupiah = "Rp 120.000",
        photoUrl = "https://example.com/bambang.jpg",
        reviewCount = 18,
        totalReviewRating = 4.5,
        booked = true,
        nomorTelpon = "089876543210",
        dokumen = Dokumen(ktp = "ada", sim = null, npwp = null),
        lokasi = Lokasi(lat = -0.0255, lon = 109.3411)
    )

    return listOf(tukang1, tukang2)
}