package com.mark.xml_to_pdf.XMLtoPDF;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mark.xml_to_pdf.XMLtoPDF.model.FailureResponse;
import com.mark.xml_to_pdf.XMLtoPDF.model.SuccessResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PdfGenerator {

    public static double postScriptThreshold = 0.75;
    public final static int a4HeightInPX = 3508;
    public final static int a4WidthInPX = 2480;
    public final static int a5HeightInPX = 1748;
    public final static int a5WidthInPX = 2480;

    public static int a4HeightInPostScript = (int) (a4HeightInPX * postScriptThreshold);
    public static int a4WidthInPostScript = (int) (a4WidthInPX * postScriptThreshold);

    public static int WRAP_CONTENT_WIDTH = 0, WRAP_CONTENT_HEIGHT = 0;

    public static ContextStep getBuilder() {
        return new Builder();
    }

    public enum PageSize {
        /**
         * For standard A4 size page
         * @deprecated For printing well-formed ISO standard sized papers(like-A4,A5 sized pdf,you
         * don't need to be concerned about width and height.Please set width and height to the xml
         * with an aspect ratio 1:√2. For example if your xml width is 100 dp then the height of the
         * xml will be (100 X √2) = 142 dp. Finally when we print them with any kind of ISO standard
         * paper, then they will be auto scaled and fit into the specific paper.
         * Reference:http://tolerancing.net/engineering-drawing/paper-size.html
         */
        @Deprecated
        A4,

        /**
         * For standard A5 size page
         * @deprecated For printing well-formed ISO standard sized papers(like-A4,A5 sized pdf,you
         * don't need to be concerned about width and height.Please set width and height to the xml
         * with an aspect ratio 1:√2. For example if your xml width is 100 dp then the height of the
         * xml will be (100 X √2) = 142 dp. Finally when we print them with any kind of ISO standard
         * paper, then they will be auto scaled and fit into the specific paper.
         * Reference:http://tolerancing.net/engineering-drawing/paper-size.html
         */
        @Deprecated
        A5,
        /**
         * For print the page as much as they are big.
         */
        WRAP_CONTENT
    }


    public interface ContextStep {
        FromSourceStep setContext(Context context);
    }

    public interface FromSourceStep {
        LayoutXMLSourceIntakeStep fromLayoutXMLSource();

        ViewIDSourceIntakeStep fromViewIDSource();

        ViewSourceIntakeStep fromViewSource();
    }


    public interface ViewSourceIntakeStep {
        FileNameStep fromView(View... viewList);
        FileNameStep fromViewList(List<View> viewList);
    }

    public interface LayoutXMLSourceIntakeStep {
        FileNameStep fromLayoutXML(@LayoutRes Integer... layoutXMLs);
        FileNameStep fromLayoutXMLList(@LayoutRes List<Integer> layoutXMLList);
    }

    public interface ViewIDSourceIntakeStep {
        /**
         * @param activity              Host activity.
         * @param relatedParentLayoutID The layout id of parent xml where the view ids are belonging (e.g- R.layout.my_layout).
         * @param xmlResourceList       The view ids which will be printed.
         * @return
         */
        FileNameStep fromViewID(@LayoutRes Integer relatedParentLayoutID, Activity activity, @IdRes Integer... xmlResourceList);
        FileNameStep fromViewIDList(@LayoutRes Integer relatedParentLayoutID, Activity activity, @IdRes List<Integer> xmlResourceList);


    }


    public interface PageSizeStep {
        FileNameStep setPageSize(PageSize pageSize);
    }


    public interface FileNameStep {
        Build setFileName(String fileName);
    }

    public interface Build {
        void build(PdfGeneratorListener pdfGeneratorListener);

        Build setFolderName(String folderName);

        Build openPDFafterGeneration(boolean open);

    }


    public static class Builder implements Build, FileNameStep, PageSizeStep
            , LayoutXMLSourceIntakeStep, ViewSourceIntakeStep, ViewIDSourceIntakeStep
            , FromSourceStep, ContextStep {

        private static int NO_XML_SELECTED_YET = -1;
        private int pageWidthInPixel = WRAP_CONTENT_WIDTH;
        private int pageHeightInPixel = WRAP_CONTENT_HEIGHT;
        private Context context;
        private PageSize pageSize;
        private PdfGeneratorListener pdfGeneratorListener;
        private List<View> viewList = new ArrayList<>();
        private String fileName;
        private String targetPdf;
        private boolean openPdfFile = true;
        private String folderName;
        private String directoryPath;
        private Disposable disposable;

        private void postFailure(String errorMessage) {
            FailureResponse failureResponse = new FailureResponse(errorMessage);
            postLog(errorMessage);
            if (pdfGeneratorListener != null)
                pdfGeneratorListener.onFailure(failureResponse);
        }

        private void postFailure(Throwable throwable) {
            FailureResponse failureResponse = new FailureResponse(throwable);
            if (pdfGeneratorListener != null)
                pdfGeneratorListener.onFailure(failureResponse);
        }

        private void postLog(String logMessage) {
            if (pdfGeneratorListener != null)
                pdfGeneratorListener.showLog(logMessage);
        }

        private void postOnGenerationStart() {
            if (pdfGeneratorListener != null)
                pdfGeneratorListener.onStartPDFGeneration();
        }

        private void postOnGenerationFinished() {
            if (pdfGeneratorListener != null)
                pdfGeneratorListener.onFinishPDFGeneration();
        }

        private void postSuccess(PdfDocument pdfDocument, File file, int widthInPS, int heightInPS) {
            if (pdfGeneratorListener != null)
                pdfGeneratorListener.onSuccess(new SuccessResponse(pdfDocument, file, widthInPS, heightInPS));
        }

        private void openGeneratedPDF() {
            try {
                File file = new File(targetPdf);
                if (file.exists()) {
                    Uri path = FileProvider.getUriForFile(context, context.getPackageName() + ".xmlToPdf.provider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        postFailure(e);
                    }
                } else {
                    String path = TextUtils.isEmpty(directoryPath) ? "null" : directoryPath;
                    postFailure("PDF file is not existing in storage. Your Generated path is " + path);
                }
            } catch (Exception exception) {
                postFailure("Error occurred while opening the PDF. Error message : " + exception.getMessage());
            }

        }

        /**
         * We should reset the value of the page otherwise page size might be differ for each page
         */
        private void resetValue() {
            if (pageSize != null) {
                if (pageSize == PageSize.A4) {
                    pageHeightInPixel = a4HeightInPX;
                    pageWidthInPixel = a4WidthInPX;
                } else if (pageSize == PageSize.A5) {
                    pageHeightInPixel = a5HeightInPX;
                    pageWidthInPixel = a5WidthInPX;
                } else if (pageSize == PageSize.WRAP_CONTENT) {
                    pageWidthInPixel = WRAP_CONTENT_WIDTH;
                    pageHeightInPixel = WRAP_CONTENT_HEIGHT;
                }
            } else {
                /*postLog("Page size is not found. Your custom page width is " +
                        pageWidthInPixel + " and custom page height is " + pageHeightInPixel);*/
            }

            postScriptThreshold = 0.75;
            a4HeightInPostScript = (int) (a4HeightInPX * postScriptThreshold);
        }

        private void print() {

            try {
                if (context != null) {
                    PdfDocument document = new PdfDocument();
                    if (viewList == null || viewList.size() == 0)
                        postLog("View list null or zero sized");
                    for (int i = 0; i < viewList.size(); i++) {
                        resetValue();
                        View content = viewList.get(i);
                        if (pageWidthInPixel == WRAP_CONTENT_WIDTH && pageHeightInPixel == WRAP_CONTENT_HEIGHT) {

                            content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                            pageHeightInPixel = content.getMeasuredHeight();
                            pageWidthInPixel = content.getMeasuredWidth();

                            postScriptThreshold = 1.0;
                            a4HeightInPostScript = pageHeightInPixel;

                        }


                        /*Convert page size from pixel into post script because PdfDocument takes
                         * post script as a size unit*/
                        pageHeightInPixel = (int) (pageHeightInPixel * postScriptThreshold);
                        pageWidthInPixel = (int) (pageWidthInPixel * postScriptThreshold);


                        content.measure(View.MeasureSpec.makeMeasureSpec(pageWidthInPixel, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED);
                        pageHeightInPixel = (Math.max(content.getMeasuredHeight(), a4HeightInPostScript));


                        PdfDocument.PageInfo pageInfo =
                                new PdfDocument.PageInfo.Builder((pageWidthInPixel), (pageHeightInPixel), i + 1).create();
                        PdfDocument.Page page = document.startPage(pageInfo);

                        content.layout(0, 0, pageWidthInPixel, pageHeightInPixel);
                        content.draw(page.getCanvas());

                        document.finishPage(page);

                        /*Finally invalidate it and request layout for restore the previous state
                         * of the view as like as the xml. Otherwise for generating PDF by view id,
                         * the main view is being messed up because this a view is not cloneable and
                         * being modified in the above view related tasks for printing PDF. */
                        content.invalidate();
                        content.requestLayout();

                    }

                    //This is for prevent crashing while opening generated PDF.
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    setUpDirectoryPath(context);

                    if (TextUtils.isEmpty(directoryPath)) {
                        postFailure("Cannot find the storage path to create the pdf file.");
                        return;
                    }


                    directoryPath = directoryPath + "/" + folderName + "/";


                    File file = new File(directoryPath);
                    if (!file.exists()) {
                        if (!file.mkdirs()) {
                            postLog("Folder is not created." +
                                    "file.mkdirs() is returning false");
                        }
                        //Folder is made here
                    }

                    targetPdf = directoryPath + fileName + ".pdf";

                    File filePath = new File(targetPdf);
                    //File is created under the folder but not yet written.

                    disposeDisposable();
                    postOnGenerationStart();
                    disposable = Completable.fromAction(() -> document.writeTo(new FileOutputStream(filePath)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally(() -> {
                                document.close();
                                disposeDisposable();
                                postOnGenerationFinished();
                            })
                            .subscribe(() -> {
                                postSuccess(document, filePath, pageWidthInPixel, pageHeightInPixel);
                                if (openPdfFile) {
                                    openGeneratedPDF();
                                }
                            }, this::postFailure);


                } else {
                    postFailure("Context is null");
                }
            } catch (Exception e) {
                postFailure(e);
            }

        }


        private void disposeDisposable() {
            if (disposable != null && !disposable.isDisposed())
                disposable.dispose();
        }

        private void setUpDirectoryPath(Context context) {

            String state = Environment.getExternalStorageState();

            // Make sure it's available
            if (!TextUtils.isEmpty(state) && Environment.MEDIA_MOUNTED.equals(state)) {
                postLog("Your external storage is mounted");
                // We can read and write the media
                directoryPath = context.getExternalFilesDir(null) != null ?
                        context.getExternalFilesDir(null).getAbsolutePath() : "";

                if (TextUtils.isEmpty(directoryPath))
                    postLog("context.getExternalFilesDir().getAbsolutePath() is returning null.");

            } else {
                postLog("Your external storage is unmounted");
                // Load another directory, probably local memory
                directoryPath = context.getFilesDir() != null ? context.getFilesDir().getAbsolutePath() : "";
                if (TextUtils.isEmpty(directoryPath))
                    postFailure("context.getFilesDir().getAbsolutePath() is also returning null.");
                else postLog("PDF file creation path is " + directoryPath);
            }
        }

        private boolean hasAllPermission(Context context) {
            if (context == null) {
                postFailure("Context is null");
                return false;
            }
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }

        @Override
        public void build(PdfGeneratorListener pdfGeneratorListener) {
            this.pdfGeneratorListener = pdfGeneratorListener;
            if (hasAllPermission(context)) {
                print();
            } else {
                postLog("WRITE_EXTERNAL_STORAGE and READ_EXTERNAL_STORAGE Permission is not given." +
                        " Permission taking popup (using https://github.com/Karumi/Dexter) is going " +
                        "to be shown");
                Dexter.withContext(context)
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                                for (PermissionDeniedResponse deniedResponse : multiplePermissionsReport.getDeniedPermissionResponses()) {
                                    postLog("Denied permission: " + deniedResponse.getPermissionName());
                                }
                                for (PermissionGrantedResponse grantedResponse : multiplePermissionsReport.getGrantedPermissionResponses()) {
                                    postLog("Granted permission: " + grantedResponse.getPermissionName());
                                }
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    print();
                                } else
                                    postLog("All necessary permission is not granted by user.Please do that first");

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                            }
                        })
                        .withErrorListener(error -> postLog("Error from Dexter (https://github.com/Karumi/Dexter) : " +
                                error.toString())).check();
            }

        }


        @Override
        public FileNameStep fromView(View... viewArrays) {
            viewList = new ArrayList<>(Arrays.asList(viewArrays));
            return this;
        }

        @Override
        public FileNameStep fromViewList(List<View> viewList) {
            this.viewList = viewList;
            return this;
        }


        @Override
        public Build openPDFafterGeneration(boolean openPdfFile) {
            this.openPdfFile = openPdfFile;
            return this;
        }


        @Override
        public FromSourceStep setContext(Context context) {
            this.context = context;
            return this;
        }

        @Override
        public FileNameStep setPageSize(PageSize pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        @Override
        public Build setFolderName(String folderName) {
            this.folderName = folderName;
            return this;
        }

        @Override
        public Build setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }


        @Override
        public FileNameStep fromViewID(@LayoutRes Integer relatedParentLayoutID, Activity activity, @IdRes Integer... xmlResourceList) {
            viewList = Utils.getViewListFromID(activity, relatedParentLayoutID, Arrays.asList(xmlResourceList), pdfGeneratorListener);
            return this;
        }

        @Override
        public FileNameStep fromViewIDList(@LayoutRes Integer relatedParentLayout, Activity activity, List<Integer> viewIDList) {
            viewList = Utils.getViewListFromID(activity, relatedParentLayout, viewIDList, pdfGeneratorListener);
            return this;
        }


        @Override
        public FileNameStep fromLayoutXML(@LayoutRes Integer... layouts) {
            viewList = Utils.getViewListFromLayout(context, pdfGeneratorListener, Arrays.asList(layouts));
            return this;
        }

        @Override
        public FileNameStep fromLayoutXMLList(@LayoutRes List<Integer> layoutXMLList) {
            viewList = Utils.getViewListFromLayout(context, pdfGeneratorListener, layoutXMLList);
            return this;
        }

        @Override
        public LayoutXMLSourceIntakeStep fromLayoutXMLSource() {
            return this;
        }

        @Override
        public ViewIDSourceIntakeStep fromViewIDSource() {
            return this;
        }

        @Override
        public ViewSourceIntakeStep fromViewSource() {
            return this;
        }
    }


}
