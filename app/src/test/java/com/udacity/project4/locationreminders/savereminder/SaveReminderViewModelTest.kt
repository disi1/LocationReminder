package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.MainCoroutineRule
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var saveReminderViewModel: SaveReminderViewModel

    private val fakeDataSource = FakeDataSource()

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        stopKoin()

        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    @Test
    fun saveReminder_resultSuccess() = runBlockingTest {
        val reminder = ReminderDataItem("title", "description", "location", 0.0, 0.0)

        saveReminderViewModel.saveReminder(reminder)

        val resultReminderDataItem = fakeDataSource.getReminder(reminder.id) as Result.Success
        val reminderDTO = resultReminderDataItem.data

        assertThat(reminderDTO.id, `is`(reminder.id))
    }

    @Test
    fun saveReminder_showsLoading() = runBlockingTest {
        val reminder = ReminderDataItem("title", "description", "location", 0.0, 0.0)

        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(reminder)
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun saveReminder_showsToast() = runBlockingTest {
        val reminder = ReminderDataItem("title", "description", "location", 0.0, 0.0)

        saveReminderViewModel.saveReminder(reminder)

        assertThat(saveReminderViewModel.showToast.getOrAwaitValue(), `is`("Reminder Saved!"))
        assertThat(saveReminderViewModel.navigationCommand.getOrAwaitValue(), `is`(NavigationCommand.Back))
    }

    @Test
    fun saveReminder_navigatesBack() = runBlockingTest {
        val reminder = ReminderDataItem("title", "description", "location", 0.0, 0.0)

        saveReminderViewModel.saveReminder(reminder)

        assertThat(saveReminderViewModel.navigationCommand.getOrAwaitValue(), `is`(NavigationCommand.Back))
    }

    @Test
    fun validateEnteredData_success() = runBlockingTest {
        val reminder = ReminderDataItem("title", "description", "location", 0.0, 0.0)

        val result = saveReminderViewModel.validateEnteredData(reminder)

        assertThat(result, `is`(true))
    }

    @Test
    fun validateEnteredData_errorTitle() = runBlockingTest {
        val reminder = ReminderDataItem(null, "description", "location", 0.0, 0.0)

        val result = saveReminderViewModel.validateEnteredData(reminder)

        assertThat(result, `is`(false))
        assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue(), `is`(R.string.err_enter_title))
    }

    @Test
    fun validateEnteredData_errorLocation() = runBlockingTest {
        val reminder = ReminderDataItem("title", "description", null, 0.0, 0.0)

        val result = saveReminderViewModel.validateEnteredData(reminder)

        assertThat(result, `is`(false))
        assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue(), `is`(R.string.err_select_location))
    }

    @Test
    fun validateAndSaveReminder_validReminder() = runBlockingTest {
        val reminder = ReminderDataItem("title", "description", "location", 0.0, 0.0)

        val result = saveReminderViewModel.validateAndSaveReminder(reminder)

        assertThat(result, `is`(true))
        val resultReminderDataItem = fakeDataSource.getReminder(reminder.id) as Result.Success
        val reminderDTO = resultReminderDataItem.data

        assertThat(reminderDTO.id, `is`(reminder.id))
    }

    @Test
    fun validateAndSaveReminder_invalidReminder() = runBlockingTest {
        val reminder = ReminderDataItem(null, "description", "location", 0.0, 0.0)

        val result = saveReminderViewModel.validateAndSaveReminder(reminder)

        assertThat(result, `is`(false))
        val resultReminderDataItem = fakeDataSource.getReminder(reminder.id) as Result.Error
        val reminderDTO = resultReminderDataItem.message

        assertThat(reminderDTO, `is`("Reminder not found"))
    }
}