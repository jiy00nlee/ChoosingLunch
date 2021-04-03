package com.example.toyproject_client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.toyproject_client.view.HomeFragViewmodel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.userlocation

class HomeFragment : Fragment() {
    private val viewModel: HomeFragViewmodel by viewModels()
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    //사용자 정보 변수들.
    private lateinit var useraddress: String

    private var userLat: Double = 0.0
    private var userLng: Double = 0.0

    //private var near: LatLng? = null //좌표인듯.
    //private lateinit var select_mode  -> 클릭여부에 따른 모드 변경 추가하기.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        //(가입시) 이미 정보가 있다는 가정하이다.
        viewModel.getUserLocationData().observe(viewLifecycleOwner) {
            useraddress = it.address
            userLat = it.latitude
            userLng = it.longtitude
            userlocation.text = useraddress //주소같은 경우 xml로 표현해주기.
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()

        //사용자 위치등록하기.
        location_btn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_insertLocationInfoFragment)
        }

    }


    private fun initViewPager() {

        //main_pager.adapter = ViewPagerAdapter(this) //뷰페이저와 뷰페이저어댑터 연결
        main_pager.offscreenPageLimit = 5                //뷰계층구조에 보관된 페이지, view/fragment 수를 제어할 수 있다.
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        main_pager.adapter = viewPagerAdapter
        main_pager.registerOnPageChangeCallback(PageChangeCallback())
        main_bottom_navigation.setOnNavigationItemSelectedListener { navigationSelected(it) } //바텀네비게이션뷰와 셀렉팅리스너 연결

        main_pager.isUserInputEnabled = false

    }

    private fun navigationSelected(item: MenuItem): Boolean {
        val checked = item.setChecked(true)
        when (checked.itemId) {
            R.id.btn_all -> {
                main_pager.setCurrentItem(0, false)
                return true
            }
            R.id.btn_korean-> {
                main_pager.setCurrentItem(1, false)
                return true
            }
            R.id.btn_chinese-> {
                main_pager.setCurrentItem(2, false)
                return true
            }
            R.id.btn_japanese-> {
                main_pager.setCurrentItem(3, false)
                return true
            }
            R.id.btn_west-> {
                main_pager.setCurrentItem(4, false)
                return true
            }
        }
        return false
    }

    private inner class PageChangeCallback: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            main_bottom_navigation.selectedItemId = when (position) {
                0 -> R.id.btn_all
                1 -> R.id.btn_korean
                2 -> R.id.btn_chinese
                3 -> R.id.btn_japanese
                4 -> R.id.btn_west
                else -> error("no such position: $position")
            }
        }
    }


}





