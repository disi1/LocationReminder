package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var remindersLocalRepository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        remindersLocalRepository = RemindersLocalRepository(database.reminderDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveReminder_getReminder() = runBlocking {
        val reminder = ReminderDTO("title", "description", "location", 0.0, 0.0)

        remindersLocalRepository.saveReminder(reminder)

        val reminderFromRepo = remindersLocalRepository.getReminder(reminder.id)

        assertThat(reminderFromRepo, not(nullValue()))
        reminderFromRepo as Result.Success
        assertThat(reminderFromRepo.data.id, `is`(reminder.id))
        assertThat(reminderFromRepo.data.title, `is`(reminder.title))
        assertThat(reminderFromRepo.data.description, `is`(reminder.description))
        assertThat(reminderFromRepo.data.location, `is`(reminder.location))
        assertThat(reminderFromRepo.data.latitude, `is`(reminder.latitude))
        assertThat(reminderFromRepo.data.longitude, `is`(reminder.longitude))
    }

    @Test
    fun getReminder_nonExistentReminder() = runBlocking {
        val reminderFromRepo = remindersLocalRepository.getReminder("1") as Result.Error

        val reminderErrorMessage = reminderFromRepo.message

        assertThat(reminderErrorMessage, Matchers.`is`("Reminder not found!"))
    }

    @Test
    fun getReminders() = runBlocking {
        val reminder1 = ReminderDTO("title1", "description1", "location1", 0.0, 0.0)
        val reminder2 = ReminderDTO("title2", "description2", "location3", 0.0, 0.0)

        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.saveReminder(reminder2)

        val remindersListFromRepo = remindersLocalRepository.getReminders() as Result.Success
        assertThat(remindersListFromRepo.data.size, `is`(2))
        assertThat(remindersListFromRepo.data[0].id, `is`(reminder1.id))
        assertThat(remindersListFromRepo.data[1].id, `is`(reminder2.id))
    }

    @Test
    fun deleteAllReminders() = runBlocking {
        val reminder1 = ReminderDTO("title1", "description1", "location1", 0.0, 0.0)
        val reminder2 = ReminderDTO("title2", "description2", "location3", 0.0, 0.0)

        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.saveReminder(reminder2)

        val remindersListFromRepo1 = remindersLocalRepository.getReminders() as Result.Success
        assertThat(remindersListFromRepo1.data.size, `is`(2))

        remindersLocalRepository.deleteAllReminders()

        val remindersListFromRepo2 = remindersLocalRepository.getReminders() as Result.Success
        assertThat(remindersListFromRepo2.data.size, `is`(0))
    }
}