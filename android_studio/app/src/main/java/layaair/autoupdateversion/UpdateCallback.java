package layaair.autoupdateversion;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

public class UpdateCallback implements IUpdateCallback {
	
	static private final String DIALOG_DOWNLOAD_ERROR_TITLE = "下载失败";
	static private final String DIALOG_DOWNLOAD_ERROR_MSG = "DOWNLOAD UPDATE FILE FAILED";
	static private final String DIALOG_DOWNLOAD_BUTTON_TRY = "RETRY";
	static private final String DIALOG_DOWNLOAD_BUTTON_CANCEL= "CANCEL";
	
	static private final String DIALOG_UPDATE_TITLE = "UPDATE";
	static private final String DIALOG_UPDATE_MSG = "确认更新[";
	static private final String DIALOG_UPDATE_MSGEND = "]?";
	static private final String DIALOG_UPDATE_PROGRESS= "UPDATE PROGRESS";
	static private final String DIALOG_UPDATE_BUTTON_TRY = "UPDATE START";
	static private final String DIALOG_UPDATE_BUTTON_CANCEL= "UPDATE CANCEL";
	
	ProgressDialog updateProgressDialog = null;
	public void downloadProgressChanged(int progress) {
		if (updateProgressDialog != null
				&& updateProgressDialog.isShowing()) {
			updateProgressDialog.setProgress(progress);
		}

	}

	public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
		if (updateProgressDialog != null
				&& updateProgressDialog.isShowing()) {
			updateProgressDialog.dismiss();
		}
		if (sucess) {
			if (AutoUpdateAPK.getInstance() != null)
			AutoUpdateAPK.getInstance().updateAPK();
		} else {
			if (AutoUpdateAPK.getInstance() == null)
				return ;
			DialogHelper.Confirm(AutoUpdateAPK.getInstance().getContext(),
					DIALOG_DOWNLOAD_ERROR_TITLE,
					DIALOG_DOWNLOAD_ERROR_MSG,
					DIALOG_DOWNLOAD_BUTTON_TRY,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
							AutoUpdateAPK.getInstance().downloadAPK();

						}
					}, DIALOG_DOWNLOAD_BUTTON_CANCEL, null);
		}
	}

	public void downloadCanceled() {
		Log.i("", "取消下载");
	}

	public void checkUpdateCompleted(Boolean hasUpdate,	CharSequence updateInfo) {
		if (AutoUpdateAPK.getInstance() == null)
			return ;
		if (hasUpdate) 
		{
			DialogHelper.Confirm(
					AutoUpdateAPK.getInstance().getContext(),
					DIALOG_UPDATE_TITLE,
					DIALOG_UPDATE_MSG + updateInfo+ DIALOG_UPDATE_MSGEND,DIALOG_UPDATE_BUTTON_TRY,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,	int which) {
							updateProgressDialog = new ProgressDialog(AutoUpdateAPK.getInstance().getContext());
							updateProgressDialog.setMessage(DIALOG_UPDATE_PROGRESS);
							updateProgressDialog.setIndeterminate(false);
							updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
							updateProgressDialog.setMax(100);
							updateProgressDialog.setProgress(0);
							updateProgressDialog.setCancelable(false);
							updateProgressDialog.setCanceledOnTouchOutside(false);
							updateProgressDialog.show();

							AutoUpdateAPK.getInstance().downloadAPK();
						}
					}, DIALOG_UPDATE_BUTTON_CANCEL,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int which) 
						{
								AutoUpdateAPK.onUpdateEnd(3);
						}
					}
					);
		}else{
			AutoUpdateAPK.onUpdateEnd(2);
		}
	}
}
