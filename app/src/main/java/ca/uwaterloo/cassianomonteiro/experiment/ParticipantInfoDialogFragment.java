package ca.uwaterloo.cassianomonteiro.experiment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.jfedor.frozenbubble.R;

public class ParticipantInfoDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ParticipantInfoDialogListener {
        void onDialogPositiveClick(String participantId, String runId);
        void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    ParticipantInfoDialogListener mListener;
    EditText participantText;
    EditText runText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.run_dialog, null);

        participantText = dialogView.findViewById(R.id.participant_id);
        runText = dialogView.findViewById(R.id.run_id);


        builder.setView(dialogView)
                .setTitle("Run info")
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(participantText.getText().toString(),
                                runText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick();
                        ParticipantInfoDialogFragment.this.getDialog().cancel();
                    }
                });

        Dialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TextWatcher watcher = new ExperimentTextWatcher(dialog);
                participantText.addTextChangedListener(watcher);
                runText.addTextChangedListener(watcher);

                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        return dialog;
    }

    // Override the Fragment.onAttach() method to instantiate the ParticipantInfoDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ParticipantInfoDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    private class ExperimentTextWatcher implements TextWatcher {

        private DialogInterface dialog;

        public ExperimentTextWatcher(DialogInterface dialog) {
            this.dialog = dialog;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean enabled = !TextUtils.isEmpty(participantText.getText()) &&
                                !TextUtils.isEmpty(runText.getText());

            ((AlertDialog) dialog).getButton(
                    AlertDialog.BUTTON_POSITIVE).setEnabled(enabled);
        }
    }
}
