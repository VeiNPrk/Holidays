package com.vnprk.holidays.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.vnprk.holidays.R
import com.vnprk.holidays.viewmodels.PrivateListViewModel

class PrivateListFragment : Fragment() {

    private val viewModel: PrivateListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_private_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}