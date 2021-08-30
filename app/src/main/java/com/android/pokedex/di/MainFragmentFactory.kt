package com.android.pokedex.di//package com.android.pokedex.di
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentFactory
//import com.android.pokedex.ui.fragments.HomeFragment
//import javax.inject.Inject
//
//class MainFragmentFactory @Inject constructor(
//    private val someString: String
//) : FragmentFactory() {
//    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
//        return when (className) {
//            HomeFragment::class.java.name -> {
////                HomeFragment(someString)
//            }
//            else -> {
//                super.instantiate(classLoader, className)
//            }
//        }
//    }
//}