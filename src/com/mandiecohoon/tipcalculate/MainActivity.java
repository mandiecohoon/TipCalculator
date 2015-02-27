package com.mandiecohoon.tipcalculate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.text.NumberFormat; // for currency formatting

import android.text.Editable; // for EditText event handling
import android.text.TextWatcher; // EditText listener
import android.widget.EditText; // for bill amount input
import android.widget.SeekBar; // for changing custom tip percentage
import android.widget.SeekBar.OnSeekBarChangeListener; // SeekBar listener
import android.widget.TextView; // for displaying text
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	// currency and percent formatters 
	   private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	   private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();
	   private static final NumberFormat numberFormat = NumberFormat.getInstance();


	   private double billAmount = 0.0; // bill amount entered by the user
	   double setBillAmount = 0.0;
	   private double customPercent = 0.18; // initial custom tip percentage
	   private double taxPercent = 0.13;
	   private boolean taxToggle = true;
	   private double billAmountTaxed = 0.0;
	   private int splitNumber = 1;
	   private TextView amountDisplayTextView; // shows formatted bill amount
	   private TextView splitTipDisplayTextView;
	   private TextView percentCustomTextView; // shows custom tip percentage
	   private TextView tip15TextView; // shows 15% tip
	   private TextView total15TextView; // shows total with 15% tip
	   private TextView tipCustomTextView; // shows custom tip amount
	   private TextView totalCustomTextView; // shows total with custom tip
	
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
		   
	      super.onCreate(savedInstanceState); // call superclass's version
	      setContentView(R.layout.activity_main); // inflate the GUI

	      // get references to the TextViews 
	      // that MainActivity interacts with programmatically
	      amountDisplayTextView = (TextView) findViewById(R.id.amountDisplayTextView);
	      splitTipDisplayTextView = (TextView) findViewById(R.id.splitTipDisplayTextView);
	      percentCustomTextView = (TextView) findViewById(R.id.percentCustomTextView);
	      tip15TextView = (TextView) findViewById(R.id.tip15TextView);
	      total15TextView = (TextView) findViewById(R.id.total15TextView);
	      tipCustomTextView = (TextView) findViewById(R.id.tipCustomTextView);
	      totalCustomTextView = (TextView) findViewById(R.id.totalCustomTextView);
	            
	      // update GUI based on billAmount and customPercent 
	      amountDisplayTextView.setText(currencyFormat.format(billAmount));
	      splitTipDisplayTextView.setText(numberFormat.format(splitNumber));
	      updateStandard(); // update the 15% tip TextViews
	      updateCustom(); // update the custom tip TextViews

	      // set amountEditText's TextWatcher
	      EditText amountEditText = (EditText) findViewById(R.id.amountEditText);
	      amountEditText.addTextChangedListener(amountEditTextWatcher);
	      
	      // set customTipSeekBar's OnSeekBarChangeListener
	      SeekBar customTipSeekBar = (SeekBar) findViewById(R.id.customTipSeekBar);
	      customTipSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
	      
	      // set splitTipEditText's TextWatcher
	      EditText splitTipEditText = (EditText) findViewById(R.id.splitTipEditText);
	      splitTipEditText.addTextChangedListener(splitTipEditTextWatcher);
	      
	      // set 
	      ToggleButton beforeTaxToggle = (ToggleButton) findViewById(R.id.beforeTaxToggle);
	      //beforeTaxToggle.setOnCheckedChangeListener(beforeTaxToggleWatcher);
	      
	      beforeTaxToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(taxToggle) {
					taxToggle = false;
			    } else {
			    	taxToggle = true;
			    }
				updateStandard();
		    	updateCustom();
			}
		   }); 
	      
	   } // end method onCreate
	   
	   // updates 15% tip TextViews
	   private void updateStandard() {
	      // calculate 15% tip and total
	      double fifteenPercentTip;
	      double fifteenPercentTotal;

		   if(taxToggle) {
			   fifteenPercentTip = billAmount * 0.15;
			   fifteenPercentTotal = billAmount + fifteenPercentTip;
		    } else {
		    	billAmountTaxed = (billAmount * taxPercent) + billAmount;
		    	fifteenPercentTip = billAmountTaxed * 0.15;
				fifteenPercentTotal = billAmountTaxed + fifteenPercentTip;
		    }


	      // display 15% tip and total formatted as currency
	      tip15TextView.setText(currencyFormat.format(fifteenPercentTip));
	      total15TextView.setText(currencyFormat.format(fifteenPercentTotal));
	   } // end method updateStandard

	   // updates the custom tip and total TextViews
	   private void updateCustom() {
	      // show customPercent in percentCustomTextView formatted as %
	      percentCustomTextView.setText(percentFormat.format(customPercent));
	      
	      double customTip;
	      double customTotal;
	      
	      if(taxToggle) {
	    	  customTip = billAmount * customPercent;
		      customTotal = billAmount + customTip;
		    } else {
		    	billAmountTaxed = (billAmount * taxPercent) + billAmount;
		    	customTip = billAmountTaxed * customPercent;
			    customTotal = billAmountTaxed + customTip;
		    }

	      // display custom tip and total formatted as currency
	      tipCustomTextView.setText(currencyFormat.format(customTip));
	      totalCustomTextView.setText(currencyFormat.format(customTotal));
	   } // end method updateCustom
	   
	   // called when the user changes the position of SeekBar
	   private OnSeekBarChangeListener customSeekBarListener = new OnSeekBarChangeListener() {
	      // update customPercent, then call updateCustom
	      @Override
	      public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
	         // sets customPercent to position of the SeekBar's thumb
	         customPercent = progress / 100.0;
	         updateCustom(); // update the custom tip TextViews
	      } // end method onProgressChanged

	      @Override
	      public void onStartTrackingTouch(SeekBar seekBar) {
	    	  
	      } // end method onStartTrackingTouch

	      @Override
	      public void onStopTrackingTouch(SeekBar seekBar) {
	    	  
	      } // end method onStopTrackingTouch
	   }; // end OnSeekBarChangeListener

	   // event-handling object that responds to amountEditText's events
	   private TextWatcher amountEditTextWatcher = new TextWatcher()  {
	      // called when the user enters a number
	      @Override
	      public void onTextChanged(CharSequence s, int start, int before, int count) {  
	    	  
	         // convert amountEditText's text to a double
	         try {
	            billAmount = Double.parseDouble(s.toString()) / 100.0;
	            setBillAmount = Double.parseDouble(s.toString()) / 100.0;
	         } catch (NumberFormatException e) {
	            billAmount = 0.0; // default if an exception occurs
	            setBillAmount = 0.0;
	         } // end catch 

	         // display currency formatted bill amount
	         amountDisplayTextView.setText(currencyFormat.format(billAmount));
	         updateStandard(); // update the 15% tip TextViews
	         updateCustom(); // update the custom tip TextViews
	      } // end method onTextChanged

	      @Override
	      public void afterTextChanged(Editable s) {
	    	  
	      } // end method afterTextChanged

	      @Override
	      public void beforeTextChanged(CharSequence s, int start, int count,  int after) {
	    	  
	      } // end method beforeTextChanged
	      
	   }; // end amountEditTextWatcher
	   
	   
	   private TextWatcher splitTipEditTextWatcher = new TextWatcher()  {
		   
		      // called when the user enters a number
		      @Override
		      public void onTextChanged(CharSequence s, int start, int before, int count) {
		    	  try {
			    	  if(Integer.parseInt(s.toString()) >= 2) {
			    		  splitNumber = Integer.parseInt(s.toString());
			    		  billAmount = setBillAmount / splitNumber;
			    	  }
		    	  } catch (NumberFormatException e) {
		    		  splitNumber = 1;
		    		  billAmount = setBillAmount / splitNumber;
		    	  }
		    	  splitTipDisplayTextView.setText(numberFormat.format(splitNumber));
		    	  updateStandard(); // update the 15% tip TextViews
		    	  updateCustom(); // update the custom tip TextViews
		      } // end method onTextChanged

		      @Override
		      public void afterTextChanged(Editable s)  {
		    	  
		      } // end method afterTextChanged

		      @Override
		      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		    	  
		      } // end method beforeTextChanged
		   }; // end splitTipEditTextWatcher 
		  
	} // end class MainActivity