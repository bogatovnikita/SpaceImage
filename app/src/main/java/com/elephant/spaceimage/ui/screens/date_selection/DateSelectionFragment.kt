package com.elephant.spaceimage.ui.screens.date_selection

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.elephant.spaceimage.R
import com.elephant.spaceimage.databinding.FragmentDateSelectionBinding
import com.elephant.spaceimage.ui.screens.base.BaseFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class DateSelectionFragment :
    BaseFragment<FragmentDateSelectionBinding>(FragmentDateSelectionBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickButton()

    }

    private fun initClickButton() {
        binding.choiceDataBtn.setOnClickListener {
            initDataPicker()
        }
    }

    private fun initDataPicker() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(MINIMUM_DATE_FOR_PICTURE))

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.selected_date)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setCalendarConstraints(constraintsBuilder.build())
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.show(requireActivity().supportFragmentManager, "")

        datePicker.addOnPositiveButtonClickListener {
            navigateTo(it)
        }
    }

    private fun navigateTo(time: Long) {
        findNavController().navigate(
            DateSelectionFragmentDirections.actionDateSelectionFragmentToPictureDayFragment(
                convertLongToTime(time)
            )
        )
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    companion object {
        const val MINIMUM_DATE_FOR_PICTURE = 803246400000L
    }
}