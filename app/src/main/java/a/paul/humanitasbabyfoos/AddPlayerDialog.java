package a.paul.humanitasbabyfoos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class AddPlayerDialog extends DialogFragment {

    public interface Listener {
        void onAddPlayerPositiveClick(String playerName, int initialWin, int initialMatch);
    }
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_add_player_message)
                .setView(R.layout.dialog_add_player_layout)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog dialog = (Dialog) dialogInterface;
                        EditText etName = dialog.findViewById(R.id.dialog_player_name);
                        EditText etWin = dialog.findViewById(R.id.dialog_player_win);
                        EditText etMatch = dialog.findViewById(R.id.dialog_player_match);
                        String name = etName.getText().toString();
                        if(name.isEmpty()) return;
                        if(name.length() > 1) {
                            name = name.substring(0,1).toUpperCase() +
                                    name.substring(1).toLowerCase();
                        } else {
                            name = name.toUpperCase();
                        }
                        int initialWin = 0;
                        int initialMatch = 0;

                        try {
                            initialWin = Integer.parseInt(etWin.getText().toString());
                            initialMatch = Integer.parseInt(etMatch.getText().toString());
                        } catch (NumberFormatException ignore) {}

                        listener.onAddPlayerPositiveClick(name, initialWin, initialMatch);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);

        return builder.create();
    }
}
