import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import application.CvUtils;
import application.CvUtilsFX;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javafx.scene.text.Text;

public class Main extends Application {

    private final String IMG_PATH = "src\\img\\6tdavc-l.jpg";
    private final String OPENCV_PATH = "D:\\OpenCVLib\\\\opencv\\\\sources\\\\data\\\\haarcascades\\";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(15.0);
        root.setAlignment(Pos.CENTER);

        Text text = new Text();
        text.setText("Добро пожаловать!");
        text.setFont(Font.font("Sitka Text", FontWeight.BOLD, FontPosture.REGULAR, 36));
        root.getChildren().add(text);

        Button faceSearchButton = new Button("Поиск лиц на изображении");
        faceSearchButton.setOnAction(this::onClickFaceButton);
        faceSearchButton.setPadding(new Insets(20, 39, 20, 39));
        faceSearchButton.setFont(Font.font("Sitka Text", FontWeight.BOLD, FontPosture.REGULAR, 20));
        root.getChildren().add(faceSearchButton);

        Button smileSearchButton = new Button("Поиск улыбок на изображении");
        smileSearchButton.setPadding(new Insets(20, 20, 20, 20));
        smileSearchButton.setOnAction(this::onClickSmileButton);
        smileSearchButton.setFont(Font.font("Sitka Text", FontWeight.BOLD, FontPosture.REGULAR, 20));
        root.getChildren().add(smileSearchButton);

        Button eyeSearchButton = new Button("Поиск глаз на изображении");
        eyeSearchButton.setPadding(new Insets(20, 35, 20, 35));
        eyeSearchButton.setOnAction(this::onClickEyeButton);
        eyeSearchButton.setFont(Font.font("Sitka Text", FontWeight.BOLD, FontPosture.REGULAR, 20));
        root.getChildren().add(eyeSearchButton);

        Scene scene = new Scene(root, 450.0, 350.0);
        stage.setTitle("OpenCV " + Core.VERSION);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        stage.show();
    }
    private void onClickFaceButton(ActionEvent e) {
        // Загружаем изображение из файла
        Mat img = Imgcodecs.imread(IMG_PATH);

        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        // Обрабатываем изображение
        // Отображаем в отдельном окне
        faceDetector(img);

        //CvUtilsFX.showImage(img, "Текст в заголовке окна");
    }


    private void faceDetector(Mat img)
    {
        CascadeClassifier face_detector = new CascadeClassifier();
        String name = "haarcascade_frontalface_alt.xml";
        //String name = "haarcascade_frontalface_alt2.xml";
        if (!face_detector.load(OPENCV_PATH + name)) {
            System.out.println("Не удалось загрузить классификатор " + name);
            return;
        }
        MatOfRect faces = new MatOfRect();
        face_detector.detectMultiScale(img, faces);
        for (Rect r : faces.toList()) {
            Imgproc.rectangle(img, new Point(r.x, r.y),
                    new Point(r.x + r.width, r.y + r.height),
                    CvUtils.COLOR_WHITE, 2);
        }
        CvUtilsFX.showImage(img, "Результат");
        img.release();
    }

    private void onClickSmileButton(ActionEvent e)
    {
        Mat img = Imgcodecs.imread(IMG_PATH);

        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        // Обрабатываем изображение
        // Отображаем в отдельном окне
        smileDetector(img);

        //CvUtilsFX.showImage(img, "Текст в заголовке окна");
    }

    private void smileDetector(Mat img)
    {
        CascadeClassifier face_detector = new CascadeClassifier();
        String name = "haarcascade_frontalface_alt.xml";
        if (!face_detector.load(OPENCV_PATH + name)) {
            System.out.println("Не удалось загрузить классификатор " + name);
            return;
        }
        CascadeClassifier smile_detector = new CascadeClassifier();
        name = "haarcascade_smile.xml";
        if (!smile_detector.load(OPENCV_PATH + name)) {
            System.out.println("Не удалось загрузить классификатор " + name);
            return;
        }
        MatOfRect faces = new MatOfRect();
        face_detector.detectMultiScale(img, faces);
        for (Rect r : faces.toList()) {
            Mat face = img.submat(r);
            MatOfRect smile = new MatOfRect();
            smile_detector.detectMultiScale(face, smile);
            for (Rect r3 : smile.toList()) {
                Imgproc.rectangle(face, new Point(r3.x, r3.y),
                        new Point(r3.x + r3.width, r3.y + r3.height),
                        CvUtils.COLOR_WHITE, 1);
            }
        }
        CvUtilsFX.showImage(img, "Результат");
        img.release();
    }

    private void onClickEyeButton(ActionEvent e)
    {
        Mat img = Imgcodecs.imread(IMG_PATH);

        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        // Обрабатываем изображение
        // Отображаем в отдельном окне
        eyeDetector(img);

        //CvUtilsFX.showImage(img, "Текст в заголовке окна");
    }

    private void eyeDetector(Mat img)
    {
        CascadeClassifier face_detector = new CascadeClassifier();
        String name = "haarcascade_frontalface_alt.xml";
        if (!face_detector.load(OPENCV_PATH + name)) {
            System.out.println("Не удалось загрузить классификатор " + name);
            return;
        }
        CascadeClassifier eye_detector = new CascadeClassifier();
        name = "haarcascade_eye.xml";
        if (!eye_detector.load(OPENCV_PATH + name)) {
            System.out.println("Не удалось загрузить классификатор " + name);
            return;
        }
        MatOfRect faces = new MatOfRect();
        face_detector.detectMultiScale(img, faces);
        for (Rect r : faces.toList()) {
            Mat face = img.submat(r);
            MatOfRect eyes = new MatOfRect();
            eye_detector.detectMultiScale(face, eyes);
            for (Rect r2 : eyes.toList()) {
                Imgproc.rectangle(face, new Point(r2.x, r2.y),
                        new Point(r2.x + r2.width, r2.y + r2.height),
                        CvUtils.COLOR_WHITE, 1);
            }
        }
        CvUtilsFX.showImage(img, "Результат");
        img.release();
    }

}
