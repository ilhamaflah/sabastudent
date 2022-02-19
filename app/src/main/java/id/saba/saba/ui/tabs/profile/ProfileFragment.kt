package id.saba.saba.ui.tabs.profile

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import id.saba.saba.R
import id.saba.saba.databinding.FragmentProfileBinding
import id.saba.saba.ui.auth.LoginActivity
import id.saba.saba.ui.payment.CreatePaymentActivity
import id.saba.saba.ui.payment.PaymentActivity
import kotlinx.android.synthetic.main.dialog_profile_pic.*
import splitties.fragments.start

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var dialog: Dialog
    private lateinit var sharedPref: SharedPreferences

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
        loadData()

        binding.imgProfilePiv.setOnClickListener {
            dialog.show()
        }

        binding.menuProfileTopUpHistory.setOnClickListener {
            start<PaymentActivity>()
        }

        binding.menuProfileLogout.setOnClickListener {
            sharedPref.edit {
                putString("USER", "")
            }

            requireActivity().finish()
            start<LoginActivity>()
        }

        binding.btnProfileTopUp.setOnClickListener {
            start<CreatePaymentActivity>()
        }
    }

    private fun initDialog() {
        dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_profile_pic)
        dialog.btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun loadData() {
        sharedPref = requireActivity().getSharedPreferences("SABA", Context.MODE_PRIVATE)
        val USER = sharedPref.getString("USER", "")
        val NAMA = sharedPref.getString("NAMA", "")
        val ROLE = sharedPref.getString("ROLE", "")
        val PIC = sharedPref.getString("PIC", "")
        val COIN = sharedPref.getString("COIN", "")
        Picasso.get().load(PIC).noFade().into(binding.imgProfilePiv)
        Picasso.get().load(PIC).noFade().into(dialog.imgProfile)
        binding.txtProfileUsername.text = USER
        binding.txtProfileEmail.text = NAMA
        binding.txtProfileTPoints.text = COIN
    }

    override fun onResume() {
        super.onResume()

    }
}