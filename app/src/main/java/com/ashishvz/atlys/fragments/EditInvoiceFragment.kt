package com.ashishvz.atlys.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ashishvz.atlys.R
import com.ashishvz.atlys.adapters.ItemListAdapter
import com.ashishvz.atlys.adapters.ItemsAdapter
import com.ashishvz.atlys.database.Status
import com.ashishvz.atlys.database.entities.Invoice
import com.ashishvz.atlys.database.entities.Item
import com.ashishvz.atlys.databinding.FragmentEditInvoiceBinding
import com.ashishvz.atlys.utils.*
import com.ashishvz.atlys.viewmodels.InvoiceViewModel
import com.ashishvz.atlys.viewmodels.ItemViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditInvoiceFragment : Fragment() {
    lateinit var binding: FragmentEditInvoiceBinding
    private var itemsList = mutableListOf<Item>()
    private var paymentTimestamp: Long? = null
    private var invoice: Invoice? = null
    private var invoiceDate: String? = null
    private var paymentDue: String? = null
    private var paymentTerms: Int? = null
    private var itemListAdapter: ItemsAdapter? = null
    private val invoiceViewModel: InvoiceViewModel by viewModels()
    private val itemViewModel: ItemViewModel by viewModels()
    private val args: EditInvoiceFragmentArgs by navArgs()

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditInvoiceBinding.inflate(layoutInflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        lifecycleScope.launch {
            invoice = invoiceViewModel.getInvoice(args.invoiceId!!)
        }
        lifecycleScope.launch {
            itemsList = itemViewModel.getItemListForInvoice(args.invoiceId!!)
        }.invokeOnCompletion {
            itemListAdapter = ItemsAdapter(itemsList)
            binding.itemListRecyclerView.adapter = itemListAdapter
        }
        lifecycleScope.launch {
            invoiceViewModel.updateInvoiceStatus(invoice!!.invoiceId, Status.DRAFT)
        }
        binding.paymentTermsSpinner.item = PAYMENT_TERMS_LIST
        binding.textInputInvoiceDate.setEndIconOnClickListener {
            val invoiceDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Invoice Date")
                .build()
            invoiceDatePicker.addOnPositiveButtonClickListener {
                val date = SimpleDateFormat(UI_DATE_FORMAT).format(Date(Timestamp(it).time))
                paymentTimestamp = Timestamp(it).time
                binding.textInputInvoiceDateEditText.setText(date)
            }
            invoiceDatePicker.addOnCancelListener {
                invoiceDatePicker.dismiss()
            }
            invoiceDatePicker.show(childFragmentManager, "Invoice Date Picker")
        }
        binding.cancel.setOnClickListener {
            binding.buttonBackInvoiceDetail.performClick()
        }
        binding.buttonBackInvoiceDetail.setOnClickListener {
            findNavController().navigate(
                EditInvoiceFragmentDirections.actionEditInvoiceFragmentToInvoiceDetailsFragment(
                    args.invoiceId!!
                )
            )
        }
        binding.paymentTermsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                paymentDue = getPaymentTermsTimeStamp(
                    PAYMENT_TERMS_LIST[binding.paymentTermsSpinner.selectedItemPosition],
                    invoice!!.getInvoiceDate()
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        binding.saveChanges.setOnClickListener {
            with(binding) {
                val senderStreetAddress =
                    if (textInputStreetAddressEditText.text!!.isBlank()) null else textInputStreetAddressEditText.text.toString()
                val senderCity =
                    if (textInputCityEditText.text!!.isBlank()) null else textInputCityEditText.text.toString()
                val senderPostCode =
                    if (textInputPostCodeEditText.text!!.isBlank()) null else textInputPostCodeEditText.text.toString()
                val senderCountry =
                    if (textInputCountryEditText.text!!.isBlank()) null else textInputCountryEditText.text.toString()
                val clientName =
                    if (textInputClientNameEditText.text!!.isBlank()) null else textInputClientNameEditText.text.toString()
                val clientEmail =
                    if (textInputClientEmailEditText.text!!.isBlank()) null else textInputClientEmailEditText.text.toString()
                val clientStreetAddress =
                    if (textInputClientStreetAddressEditText.text!!.isBlank()) null else textInputClientStreetAddressEditText.text.toString()
                val clientCity =
                    if (textInputClientCityEditText.text!!.isBlank()) null else textInputClientCityEditText.text.toString()
                val clientPostCode =
                    if (textInputClientPostCodeEditText.text!!.isBlank()) null else textInputClientPostCodeEditText.text.toString()
                val clientCountry =
                    if (textInputClientCountryEditText.text!!.isBlank()) null else textInputClientCountryEditText.text.toString()
                invoiceDate =
                    if (textInputInvoiceDateEditText.text!!.isBlank()) null else SimpleDateFormat(
                        DB_DATE_FORMAT
                    ).format(SimpleDateFormat(UI_DATE_FORMAT).parse(textInputInvoiceDateEditText.text!!.toString())!!)
                        .toString()
                val projectDescription =
                    if (textInputProjectDescriptionEditText.text.isNullOrBlank()) null else textInputProjectDescriptionEditText.text.toString()
                if (senderStreetAddress.isNullOrBlank()) {
                    textInputStreetAddressEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (senderCity.isNullOrBlank()) {
                    textInputCityEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (senderPostCode.isNullOrBlank()) {
                    textInputPostCodeEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (senderCountry.isNullOrBlank()) {
                    textInputCountryEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (clientName.isNullOrBlank()) {
                    textInputClientNameEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (clientEmail.isNullOrBlank()) {
                    textInputClientEmailEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (clientStreetAddress.isNullOrBlank()) {
                    textInputClientStreetAddressEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (clientCity.isNullOrBlank()) {
                    textInputClientCityEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (clientPostCode.isNullOrBlank()) {
                    textInputClientPostCodeEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (clientCountry.isNullOrBlank()) {
                    textInputClientCountryEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (invoiceDate.isNullOrBlank()) {
                    textInputInvoiceDateEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (paymentTermsSpinner.selectedItemPosition < 0) {
                    paymentTermsSpinner.errorText = "Cannot be empty"
                    return@setOnClickListener
                }
                if (projectDescription.isNullOrBlank()) {
                    textInputProjectDescriptionEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                val invoiceTemp = Invoice(
                    invoice!!.invoiceId,
                    invoiceDate,
                    paymentDue,
                    projectDescription,
                    paymentTerms,
                    clientName,
                    clientEmail,
                    Status.PENDING,
                    senderStreetAddress,
                    senderCity,
                    senderPostCode,
                    senderCountry,
                    clientStreetAddress,
                    clientCity,
                    clientPostCode,
                    clientCountry,
                    getInvoiceTotal(itemListAdapter!!)
                )
                invoiceViewModel.insertInvoice(invoiceTemp)
                val itemsToInsert = getItemsToInsert(invoiceTemp.invoiceId, itemListAdapter!!)
                if (itemsToInsert != null) {
                    itemViewModel.insertAllItems(itemsToInsert)
                }
                doToast(requireContext(), "Invoice Updated")
                buttonBackInvoiceDetail.performClick()
            }
        }
        binding.addNewItemButton.setOnClickListener {
            addNewEmptyItem()
            itemListAdapter!!.notifyItemInserted(itemsList.size)
        }
        populateUi(invoice!!)
        return binding.root
    }

    private fun populateUi(invoice: Invoice) {
        with(binding) {
            invoiceIdTextView.text = invoice.invoiceId
            textInputStreetAddressEditText.setText(invoice.sa_street)
            textInputCityEditText.setText(invoice.sa_city)
            textInputPostCodeEditText.setText(invoice.sa_postCode)
            textInputCountryEditText.setText(invoice.sa_country)
            textInputClientNameEditText.setText(invoice.clientName)
            textInputClientEmailEditText.setText(invoice.clientEmail)
            textInputClientStreetAddressEditText.setText(invoice.ca_street)
            textInputClientCityEditText.setText(invoice.ca_city)
            textInputClientPostCodeEditText.setText(invoice.ca_postCode)
            textInputClientCountryEditText.setText(invoice.ca_country)
            textInputInvoiceDateEditText.setText(invoice.getInvoiceDate())
            textInputProjectDescriptionEditText.setText(invoice.description)
            when (invoice.paymentTerms) {
                30 -> paymentTermsSpinner.setSelection(0)
                60 -> paymentTermsSpinner.setSelection(1)
                90 -> paymentTermsSpinner.setSelection(2)
                else -> {}
            }
        }
    }

    private fun addNewEmptyItem() {
        itemsList.add(getEmptyItem())
    }

    @SuppressLint("SimpleDateFormat")
    private fun getPaymentTermsTimeStamp(selectedItem: String, invoiceDate: String): String {
        return when (selectedItem) {
            PAYMENT_TERMS_LIST[0] -> {
                paymentTerms = 30
                getDate(30, invoiceDate)
            }
            PAYMENT_TERMS_LIST[1] -> {
                paymentTerms = 60
                getDate(60, invoiceDate)
            }
            else -> {
                paymentTerms = 90
                getDate(90, invoiceDate)
            }
        }
    }
}