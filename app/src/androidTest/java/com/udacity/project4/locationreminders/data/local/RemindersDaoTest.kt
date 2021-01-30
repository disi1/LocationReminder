package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun saveReminderAndGetReminderById() = runBlockingTest {
        val reminder = ReminderDTO("title", "description", "location", 0.0, 0.0)
        database.reminderDao().saveReminder(reminder)

        val loadedReminder = database.reminderDao().getReminderById(reminder.id)

        assertThat(loadedReminder as ReminderDTO, notNullValue())
        assertThat(loadedReminder.id, `is`(reminder.id))
        assertThat(loadedReminder.title, `is`(reminder.title))
        assertThat(loadedReminder.description, `is`(reminder.description))
        assertThat(loadedReminder.location, `is`(reminder.location))
        assertThat(loadedReminder.latitude, `is`(reminder.latitude))
        assertThat(loadedReminder.longitude, `is`(reminder.longitude))
    }

    @Test
    fun getReminders() = runBlockingTest {
        val reminder1 = ReminderDTO("title1", "description1", "location1", 0.0, 0.0)
        val reminder2 = ReminderDTO("title2", "description2", "location3", 0.0, 0.0)
        val reminder3 = ReminderDTO("title3", "description2", "location3", 0.0, 0.0)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)

        val remindersList = database.reminderDao().getReminders()

        assertThat(remindersList.size, `is`(3))
        assertThat(remindersList[0].id, `is`(reminder1.id))
        assertThat(remindersList[1].id, `is`(reminder2.id))
        assertThat(remindersList[2].id, `is`(reminder3.id))
    }

    @Test
    fun deleteAllReminders() = runBlockingTest {
        val reminder1 = ReminderDTO("title1", "description1", "location1", 0.0, 0.0)
        val reminder2 = ReminderDTO("title2", "description2", "location3", 0.0, 0.0)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)

        database.reminderDao().deleteAllReminders()

        val remindersList = database.reminderDao().getReminders()

        assertThat(remindersList.size, `is`(0))
    }

}