package com.redrocket.photoeditor.presentation.common.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.redrocket.photoeditor.R;

/**
 * Диалог для файловой ошибки.
 */
public class FileErrorDialog extends DialogFragment {
    private OnDialogListener mListener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_file_error_msg)
                .setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onDismiss();
        }
    }

    /**
     * Установить слушателя диалога.
     *
     * @param listener Слушатель.
     */
    public void setListener(OnDialogListener listener) {
        mListener = listener;
    }

    /**
     * Интерфейс слушателя диалога.
     */
    public interface OnDialogListener {
        /**
         * Далог закрылся.
         */
        void onDismiss();
    }
}
