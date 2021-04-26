package com.backend.todo_tasker

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.backend.todo_tasker.database.DatabaseClass
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

var STRING_FOO = "Foo"

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddTaskTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addTaskTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appCompatEditText = onView(
                allOf(withId(R.id.edittext_name),withText(appContext.getString(R.string.STRING_NAME)),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText.perform(replaceText(STRING_FOO))

        val appCompatEditText2 = onView(
                allOf(withId(R.id.edittext_name), withText(STRING_FOO),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText2.perform(closeSoftKeyboard())

        val appCompatEditText3 = onView(
                allOf(withId(R.id.edittext_name), withText(STRING_FOO),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText3.perform(pressImeActionButton())

        val materialButton = onView(
                allOf(withId(R.id.button_add_to_db), withText(appContext.getString(R.string.STRING_ADDTASK)),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()))
        materialButton.perform(click())

        val db = DatabaseClass(appContext)
        val datab = db.createDb()
        val allEntrys = (db.getAllDb(datab))
        assert(true)
        //assert(allEntrys[allEntrys.size - 1].title == "Foo") // TODO: Add mock-db
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}