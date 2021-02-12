package com.pavesid.androidacademy.ui.dialogs

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.ChooserBinding
import com.pavesid.androidacademy.utils.viewBinding
import timber.log.Timber

class DateDialogFragment : DialogFragment() {

    private val binding: ChooserBinding by viewBinding(ChooserBinding::bind)

    private val date by lazy { DatePicker(requireContext(), binding.addDateText) }
    private val time by lazy { TimePicker(requireContext(), binding.addTimeText) }

    lateinit var title: String
    lateinit var overview: String
    private var duration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.chooser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = arguments?.getString(TITLE) ?: ""
        overview = arguments?.getString(OVERVIEW) ?: ""
        duration = arguments?.getInt(DURATION) ?: 0
        binding.addDateText.setOnClickListener {
            date.setDate()
        }
        binding.addTimeText.setOnClickListener {
            time.setTime()
        }
        binding.titleText.text = title
        binding.apply.setOnClickListener {
            startCalendarIntent()
        }
    }

    private fun startCalendarIntent() {
        val startMillis = date.time + time.time
        Timber.d(date.time.toString())
        Timber.d(time.time.toString())
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .addFlags(FLAG_GRANT_READ_URI_PERMISSION)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + duration * 60_000)
            .putExtra(CalendarContract.Events.TITLE, title)
            .putExtra(CalendarContract.Reminders.MINUTES, 60)
            .putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
            .putExtra(CalendarContract.Events.DESCRIPTION, overview)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, resources.getString(R.string.cinema))
            .putExtra(
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_BUSY
            )

        startActivity(intent)
        dismiss()
    }

    companion object {
        fun newInstance(title: String, overview: String, duration: Int) = DateDialogFragment().apply {
            val args = Bundle()
            args.apply {
                putString(TITLE, title)
                putString(OVERVIEW, overview)
                putInt(DURATION, duration)
            }
            arguments = args
        }

        private const val TITLE = "TiTlE"
        private const val OVERVIEW = "oVeRvIeW"
        private const val DURATION = "DuraTioN"
    }
}
