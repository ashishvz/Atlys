package com.ashishvz.atlys.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ashishvz.atlys.R
import com.ashishvz.atlys.adapters.ItemListAdapter
import com.ashishvz.atlys.adapters.ItemsAdapter
import com.ashishvz.atlys.database.Status
import com.ashishvz.atlys.database.entities.Invoice
import com.ashishvz.atlys.database.entities.Item
import com.ashishvz.atlys.databinding.FragmentNewInvoiceBinding
import com.ashishvz.atlys.utils.*
import com.ashishvz.atlys.viewmodels.InvoiceViewModel
import com.ashishvz.atlys.viewmodels.ItemViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewInvoiceFragment : Fragment() {
    lateinit var binding: FragmentNewInvoiceBinding
    private val itemsList = mutableListOf<Item>()
    private var invoice: Invoice? = null
    private var paymentTimestamp: Long? = null
    private var invoiceDate: String? = null
    private var paymentDue: String? = null
    private var paymentTerms: Int? = null
    private var itemListAdapter: ItemsAdapter? = null
    private val invoiceViewModel: InvoiceViewModel by viewModels()
    private val itemViewModel: ItemViewModel by viewModels()

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewInvoiceBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        itemsList.add(getEmptyItem())
        val paymentTermsAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_textview_layout, PAYMENT_TERMS_LIST)
        itemListAdapter = ItemsAdapter(itemsList)
        binding.itemListRecyclerView.adapter = itemListAdapter
        (binding.textInputPaymentTerms.editText as? AutoCompleteTextView)?.setDropDownBackgroundResource(
            R.color.secondary_light
        )
        (binding.textInputPaymentTerms.editText as? AutoCompleteTextView)?.setAdapter(
            paymentTermsAdapter
        )
        binding.textInputInvoiceDateEditText.setText(SimpleDateFormat(UI_DATE_FORMAT).format(Date()))
        invoiceDate =
            if (binding.textInputInvoiceDateEditText.text!!.isBlank()) null else SimpleDateFormat(
                DB_DATE_FORMAT
            ).format(SimpleDateFormat(UI_DATE_FORMAT).parse(binding.textInputInvoiceDateEditText.text!!.toString())!!)
                .toString()
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
        binding.addNewItemButton.setOnClickListener {
            addNewEmptyItem()
            itemListAdapter!!.notifyItemInserted(itemsList.size)
        }
        binding.discard.setOnClickListener {
            findNavController().navigate(NewInvoiceFragmentDirections.actionNewInvoiceFragmentToInvoiceListFragment())
        }
        binding.saveAsDraft.setOnClickListener {
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
                val invoiceId = getInvoiceId()
                val invoice = Invoice(
                    invoiceId,
                    invoiceDate,
                    paymentDue,
                    projectDescription,
                    paymentTerms,
                    clientName,
                    clientEmail,
                    Status.DRAFT,
                    senderStreetAddress,
                    senderCity,
                    senderPostCode,
                    senderCountry,
                    clientStreetAddress,
                    clientCity,
                    clientPostCode,
                    clientCountry,
                    getInvoiceTotal()
                )
                invoiceViewModel.insertInvoice(invoice)
                val itemsToInsert = getItemsToInsert(invoiceId)
                if (itemsToInsert != null) {
                    itemViewModel.insertAllItems(itemsToInsert)
                }
            }
            doToast(requireContext(), "Invoice Created")
            binding.discard.performClick()
        }
        (binding.textInputPaymentTerms.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, pos, id ->
            paymentDue = getPaymentTermsTimeStamp(
                adapterView.getItemAtPosition(pos).toString(),
                invoiceDate!!
            )
        }
        binding.saveAndSend.setOnClickListener {
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
                if (textInputPaymentTermsTextView.text.isNullOrBlank()) {
                    textInputPaymentTermsTextView.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (projectDescription.isNullOrBlank()) {
                    textInputProjectDescriptionEditText.error = "Cannot be empty"
                    return@setOnClickListener
                }
                val invoiceId = getInvoiceId()
                val invoice = Invoice(
                    invoiceId,
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
                    getInvoiceTotal()
                )
                invoiceViewModel.insertInvoice(invoice)
                val itemsToInsert = getItemsToInsert(invoiceId)
                if (itemsToInsert != null) {
                    itemViewModel.insertAllItems(itemsToInsert)
                }
                doToast(requireContext(), "Invoice Created")
                buttonBackInvoiceDetail.performClick()
            }
        }
        binding.buttonBackInvoiceDetail.setOnClickListener {
            binding.discard.performClick()
        }
        return binding.root
    }

    private fun addNewEmptyItem() {
        itemsList.add(getEmptyItem())
    }

    private fun getEmptyItem(): Item {
        return Item(0, null, null, null, null, null)
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

    private fun getInvoiceTotal(): Double {
        val itemData = itemListAdapter!!.getItems()
        var total = 0.0
        if (itemData.isEmpty())
            return total
        for (item in itemData) {
            if (item.total != null)
                total += item.total!!
        }
        return total
    }

    private fun getItemsToInsert(invoiceId: String): List<Item>? {
        val itemList = itemListAdapter!!.getItems()
        val items = mutableListOf<Item>()
        for (item in itemList) {
            if (item.name != null && item.quantity != null && item.price != null && item.total != null) {
                item.invoiceId = invoiceId
                items.add(item)
            }
        }
        return items
    }
}