package com.example.toyproject_client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.toyproject_client.myserver.PlaceDocument
import com.example.toyproject_client.data.UserDataViewmodel
import kotlinx.android.synthetic.main.fragment_firsthome.*
import kotlinx.android.synthetic.main.fragment_fourthhome.*
import kotlinx.android.synthetic.main.fragment_secondhome.recyclerView

class HomeSecondFragment : Fragment() {

    private val viewModel: UserDataViewmodel by viewModels()
    private lateinit var adapterStore: Store_RecyclerViewAdapter

    private var userLat: Double = 0.0
    private var userLng: Double = 0.0
    private var useraddress : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_secondhome, container, false)
        return  rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecyclerView()
    }

    private fun showRecyclerView() {
        adapterStore = Store_RecyclerViewAdapter() { placeDocument ->
            val bundle = Bundle()
            bundle.putParcelable("selectedStore", placeDocument)
            findNavController().navigate(
                    R.id.action_homeFragment_to_storeInfoFragment,
                    bundle
            )
        }.apply {
            viewModel.getUserLocationData().observe(viewLifecycleOwner) {
                userLat = it.latitude
                userLng = it.longtitude
                useraddress = it.address
                viewModel.getStoreList("한식", userLng, userLat, useraddress).observe(viewLifecycleOwner){observedresultStorelist ->
                    received_items = observedresultStorelist
                }
            }
        }
        recyclerView.adapter = adapterStore
    }


}