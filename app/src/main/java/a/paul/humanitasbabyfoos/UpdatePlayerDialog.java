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

import java.util.ArrayList;

public class UpdatePlayerDialog extends DialogFragment {
    public interface Listener {
        void onUpdatePlayerPositiveClick(String oldName, String newName);
    }
    private UpdatePlayerDialog.Listener listener;

    private Dialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (UpdatePlayerDialog.Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_update_player_message)
                .setView(R.layout.dialog_update_player_layout)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog dialog = (Dialog) dialogInterface;
                        EditText oldNameEditText = dialog.findViewById(R.id.dialog_update_player_old_name);
                        EditText newNameEditText = dialog.findViewById(R.id.dialog_update_player_new_name);

                        String oldName = oldNameEditText.getText().toString();
                        String newName = newNameEditText.getText().toString();

                        if(oldName.isEmpty() || newName.isEmpty()) return;

                        if(oldName.length() > 1) {
                            oldName = oldName.substring(0,1).toUpperCase() +
                                    oldName.substring(1).toLowerCase();
                        } else {
                            oldName = oldName.toUpperCase();
                        }

                        if(newName.length() > 1) {
                            newName = newName.substring(0,1).toUpperCase() +
                                    newName.substring(1).toLowerCase();
                        } else {
                            newName = newName.toUpperCase();
                        }

                        listener.onUpdatePlayerPositiveClick(oldName, newName);
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

        for(int i = 0; i < MainActivity.playersList.size(); i++) {
            playersList.add(MainActivity.playersList.get(i).name);
        }
        ArrayAdapter<String> playersAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, playersList);

        final AutoCompleteTextView oldName = dialog.findViewById(R.id.dialog_update_player_old_name);
        oldName.setAdapter(playersAdapter);

    }
}
