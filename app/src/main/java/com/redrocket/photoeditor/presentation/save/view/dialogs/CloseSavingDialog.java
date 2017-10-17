package com.redrocket.photoeditor.presentation.save.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.redrocket.photoeditor.R;

/**
 * Диалог отмены сохранения.
 */
public class CloseSavingDialog extends DialogFragment {
    private OnDialogListener mListener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_close_saving_msg)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handlePositiveClick();
                            }
                        })
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    /**
     * Установить слушателя диалога.
     *
     * @param listener Слушатель.
     */
    public void setListener(@NonNull OnDialogListener listener) {
        mListener = listener;
    }

    private void handlePositiveClick() {
        mListener.onStopSavingClick();
    }

    /**
     * Интерфейс слушателя диалога.
     */
    public interface OnDialogListener {
        /**
         * Пользователь нажал кнопку отмены сохранения.
         */
        void onStopSavingClick();
    }
}
