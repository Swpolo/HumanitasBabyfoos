package a.paul.humanitasbabyfoos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AddMatchDialog extends DialogFragment {
    public interface Listener {
        void onAddMatchPositiveClick(String match);
    }
    private AddMatchDialog.Listener listener;

    private Dialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddMatchDialog.Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_add_match_message)
                .setView(R.layout.dialog_add_match_layout)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onAddMatchPositiveClick(returnMatch(((Dialog) dialogInterface)));
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);

        dialog = builder.create();

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();


        ArrayList<String> playersList = new ArrayList<>();

        for(int i = 0; i < MainActivity.playerList.size(); i++) {
            playersList.add(MainActivity.playerList.get(i).name);
        }
        ArrayAdapter<String> playersAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, playersList);

        final AutoCompleteTextView blueAttacker = dialog.findViewById(R.id.blue_attacker);
        blueAttacker.setAdapter(playersAdapter);

        final AutoCompleteTextView blueDefencer = dialog.findViewById(R.id.blue_defencer);
        blueDefencer.setAdapter(playersAdapter);

        final AutoCompleteTextView redAttacker = dialog.findViewById(R.id.red_attacker);
        redAttacker.setAdapter(playersAdapter);

        final AutoCompleteTextView redDefencer = dialog.findViewById(R.id.red_defencer);
        redDefencer.setAdapter(playersAdapter);

        final TextView blueScoreText = dialog.findViewById(R.id.blue_score);
        blueScoreText.setText(getText(R.string.blue_score_base) + Integer.toString(0));
        SeekBar blueScoreBar = dialog.findViewById(R.id.blue_score_bar);
        blueScoreBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blueScoreText.setText(getText(R.string.blue_score_base) + Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView redScoreText = dialog.findViewById(R.id.red_score);
        redScoreText.setText(getText(R.string.red_score_base) + " " + Integer.toString(0));
        SeekBar redScoreBar = dialog.findViewById(R.id.red_score_bar);
        redScoreBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                redScoreText.setText(getText(R.string.red_score_base) + " " + Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String returnMatch(Dialog dialog) {
        return String.valueOf(((SeekBar) dialog.findViewById(R.id.blue_score_bar)).getProgress()) +
        ";" +
        ((EditText) dialog.findViewById(R.id.blue_attacker)).getText().toString() +
        ";" +
        ((EditText) dialog.findViewById(R.id.blue_defencer)).getText().toString() +
        ";" +
        ((SeekBar) dialog.findViewById(R.id.red_score_bar)).getProgress() +
        ";" +
        ((EditText) dialog.findViewById(R.id.red_attacker)).getText().toString() +
        ";" +
        ((EditText) dialog.findViewById(R.id.red_defencer)).getText().toString();
    }
}
