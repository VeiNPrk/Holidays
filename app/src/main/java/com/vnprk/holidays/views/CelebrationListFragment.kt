package com.vnprk.holidays.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.vnprk.holidays.R
import com.vnprk.holidays.viewmodels.CelebrationListViewModel

class CelebrationListFragment : Fragment() {

    private val viewModel: CelebrationListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_celebration_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        var url: Int = -1
        if (arguments != null) {
            // The getPrivacyPolicyLink() method will be created automatically.
            //url =CelebrationListFragmentArgs.fromBundle(requireArguments()).typeCelebrate
            url = arguments?.getInt("typeCelebrate", -1)!!
            //url = CelebrationListFragmentArgs.fromBundle(arguments).getPrivacyPolicyLink()
        }
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it+url
        })
        
        return root
    }
}