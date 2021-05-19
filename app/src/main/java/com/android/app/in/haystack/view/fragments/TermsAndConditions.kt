package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.databinding.FragmentTermsAndConditionsBinding
import com.android.app.`in`.haystack.network.config.AppConfig.BASE_URL
import com.android.app.`in`.haystack.network.config.AppConfig.TERMS_AND_CONDITIONS

class TermsAndConditions: Fragment() {

    private lateinit var binding: FragmentTermsAndConditionsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTermsAndConditionsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(BASE_URL + TERMS_AND_CONDITIONS)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)

        binding.toolbarTermsAndConditions.setNavigationOnClickListener {
            if (binding.webView.canGoBack()) binding.webView.goBack()
            else findNavController().popBackStack()
        }
    }
}