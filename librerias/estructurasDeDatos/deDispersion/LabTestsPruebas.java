package librerias.estructurasDeDatos.deDispersion;

import aplicaciones.biblioteca.Termino;
import aplicaciones.editorPredictivo.EditorPredictivo;
import aplicaciones.primitiva.ApuestaPrimitiva;
import aplicaciones.primitiva.NumeroPrimitiva;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import librerias.estructurasDeDatos.jerarquicos.ABB;
import librerias.estructurasDeDatos.modelos.ListaConPI;
import librerias.util.Ordenacion;

public class LabTestsPruebas {
  private static ObjectOutputStream salida = null;
  
  private static String alumno;
  
  private static String pc;
  
  private static boolean verb = false;
  
  private static final int CAS = 0;
  
  private static final int ENG = 1;
  
  private static final int turno = 0;
  
  private static int lang = 0;
  
  private static final int TIME_OUT = 10;
  
  private static final double MIN_NOTA = 0.0D;
  
  private static final String[] CAP = new String[] { "EDA GII. Primera prueba de laboratorio. Curso 2019-20.", "EDA GII - First Lab Exam. Academic Year 2019-20." };
  
  private static final String LIN = "========================================================";
  
  private static final String[] ALUM = new String[] { "Alumno: ", "Student: " };
  
  private static final String[] ENTREGA = new String[] { "Calificado.", "Submitted." };
  
  private static final String[] PRUEBA = new String[] { "PRUEBA ", "TEST " };
  
  private static final String[] NO_AUT = new String[] { "*** Examen fuera de plazo o no autorizado.", "*** Unauthorized access or lab exam out of time." };
  
  private static final String[] EXC_TM = new String[] { "TIEMPO TEST EXCEDIDO: infinito? ", "Test Run Time Limit Exceeded: probable infinite loop. " };
  
  private static final String[] EXC = new String[] { "EXCEPCION: ", "EXCEPTION: " };
  
  private static final String[] ERR = new String[] { "ERROR: ", "ERROR: " };
  
  private static final String[] NO_METHOD = new String[] { "No existe el metodo que se quiere ejecutar.", "The method to be tested does not exist." };
  
  private static final String[] NOM_PRACT = new String[] { "Pr", "Lab " };
  
  private static final int[] EJER_PRACT = new int[] { 1, 1, 2, 2, 3, 3, 4, 4 };
  
  private static final double[] EJER_PUNTOS = new double[] { 0.3D, 0.4D, 0.5D, 0.6D, 0.5D, 0.6D, 0.5D, 0.6D };
  
  public static void main() {
    ArrayList<Object> mensaje = new ArrayList();
    String notaEnEjer = "";
    double notaLabTests = 0.0D;
    int numEjer = EJER_PRACT.length;
    for (int i = 1; i <= numEjer; i++) {
        notaLabTests += ejer(i, mensaje); 
    }
    datosAlum();
    Instant ahora = Instant.ofEpochMilli(System.currentTimeMillis() + 3600000L);
    File eixida = new File("./librerias/estructurasDeDatos/deDispersion/TablaHashExamen.LabTestsPruebas");
    String path = eixida.getAbsolutePath();
    try {
      salida = new ObjectOutputStream(new FileOutputStream(eixida));
      salidaPantalla("\n" + alumno + " " + pc + "\n");
      salida.writeObject(alumno + " " + alumno + "\n");
      salidaPantalla(path + "\n\n");
      salida.writeObject(path + "\n\n");
      for (Object s : mensaje) {
        salidaPantalla(s);
        salida.writeObject(s);
      } 
      salida.writeObject("\n");
      salidaPantalla("\n" + notaLabTests + "\n\n");
      salida.writeObject("->");
      salida.writeObject(Double.valueOf(notaLabTests));
      salida.writeObject("<-\n");
      salida.writeObject("\n");
      salidaPantalla("" + ahora + "\n\n");
      salida.writeObject(ahora);
      salida.writeObject("\n");
    } catch (IOException e) {
      System.out.println("El fichero no existe o no se puede crear");
      notaLabTests = 0.0D;
    } finally {
      try {
        if (salida != null) {
            salida.close(); 
        }
      } catch (IOException e) {
          System.out.println("Fichero no cerrado correctamente");
          notaLabTests = 0.0D;
      } 
    } 
    System.out.println();
    System.out.println("  Si has terminado, sube el fichero \n  TablaHashExamen.LabTests a PoliformaT\n");
    System.out.println("Mi nota es: " + notaLabTests);
  }
  
  private static double ejer(int ejer, ArrayList<Object> mensaje) {
    System.out.print("---> " + PRUEBA[lang] + ejer + " (" + NOM_PRACT[lang] + EJER_PRACT[ejer - 1] + "). ");
    double nota = 0.0D;
    String razonDelCero = "";
    try {
      nota = exeBasTemp(10L, TimeUnit.SECONDS, ejer).doubleValue();
    } catch (Exception e) {
      razonDelCero = EXC[lang] + EXC[lang] + ". ";
      System.out.print(EXC[lang] + EXC[lang] + ". ");
    } catch (Error e) {
      razonDelCero = EXC[lang] + EXC[lang] + ". ";
      System.out.println(ERR[lang] + ERR[lang] + ". ");
    } 
    System.out.println(ENTREGA[lang]);
    String res = "";
    if (!razonDelCero.equals(""))
      mensaje.add(razonDelCero); 
    mensaje.add(PRUEBA[lang] + " ");
    mensaje.add(Integer.valueOf(ejer));
    mensaje.add(": ");
    mensaje.add(Double.valueOf(nota));
    mensaje.add("\n");
    return nota;
  }
  
  private static Double exeBasTemp(long timeout, TimeUnit unit, int ejer) throws Exception {
    ExecutorService service = Executors.newSingleThreadExecutor();
    Double nota = Double.valueOf(0.0D);
    try {
      Future<Double> f = service.submit(() -> Double.valueOf(exeBas(ejer)));
      nota = f.get(timeout, unit);
    } catch (TimeoutException e) {
      System.out.print(EXC_TM[lang]);
      service.shutdown();
    } 
    return nota;
  }
  
  private static double exeBas(int ejer) throws Exception {
    double nota = 0.0D;
    switch (ejer) {
      case 1:
        nota = testNumeroPrimitiva();
        break;
      case 2:
        nota = testApuestaPrimitiva();
        break;
      case 3:
        nota = testMerge2();
        break;
      case 4:
        nota = testMergeSort2();
        break;
      case 5:
        nota = testTermino();
        break;
      case 6:
        nota = testTablaHash();
        break;
      case 7:
        nota = testRecEquilibrado();
        break;
      case 8:
        nota = testEditorPredictivo();
        break;
    } 
    return nota;
  }
  
  private static double testNumeroPrimitiva() throws Exception {
    double MAX_NOTA = EJER_PUNTOS[0];
    boolean res = true;
    int TALLA = 2500;
    for (int i = 1; i <= 2500; i++) {
      NumeroPrimitiva a = new NumeroPrimitiva();
      NumeroPrimitiva b = new NumeroPrimitiva();
      if (a.toString().equals(b.toString())) {
        if (!a.equals(b))
          res = false; 
        if (a.compareTo(b) != 0)
          res = false; 
      } else {
        if (a.equals(b))
          res = false; 
        int x = Integer.parseInt(a.toString());
        int y = Integer.parseInt(b.toString());
        if (x < y) {
          if (a.compareTo(b) >= 0)
            res = false; 
        } else if (a.compareTo(b) <= 0) {
          res = false;
        } 
      } 
    } 
    if (res)
      return MAX_NOTA; 
    return 0.0D;
  }
  
  private static double testApuestaPrimitiva() throws Exception {
    double MAX_NOTA = EJER_PUNTOS[1];
    boolean res = (testApuestaPrimitiva(false) && testApuestaPrimitiva(true));
    if (res)
      return MAX_NOTA; 
    return 0.0D;
  }
  
  private static boolean testApuestaPrimitiva(boolean ordenada) {
    int TALLA = 2500;
    for (int i = 0; i <= 2500; i++) {
      ApuestaPrimitiva a = new ApuestaPrimitiva(ordenada);
      ArrayList<Integer> c = obtenerCombinacion(a);
      if (c.size() != 6)
        return false; 
      boolean[] v = new boolean[49];
      int prev = -1;
      for (int j = 0; j < c.size(); j++) {
        int n = ((Integer)c.get(j)).intValue() - 1;
        if (v[n])
          return false; 
        v[n] = true;
        if (ordenada && n < prev)
          return false; 
        prev = n;
      } 
    } 
    return true;
  }
  
  private static ArrayList<Integer> obtenerCombinacion(ApuestaPrimitiva a) {
    ArrayList<Integer> c = new ArrayList<>();
    String[] nums = a.toString().split(",");
    for (int i = 0; i < nums.length; i++)
      c.add(Integer.valueOf(Integer.parseInt(nums[i].trim()))); 
    return c;
  }
  
  private static double testMerge2() throws Exception {
    String metodo = "merge2";
    String clase = "librerias.util.Ordenacion";
    double nota = 0.0D;
    Method m = buscarMetodo(Class.forName(clase), metodo);
    if (m == null) {
      System.out.println(NO_METHOD[lang]);
    } else {
      nota = compruebaMerge2(m);
    } 
    return nota;
  }
  
  private static double compruebaMerge2(Method m) throws Exception {
    double MAX_NOTA = EJER_PUNTOS[2];
    int TALLA = 66666;
    Integer[] a1 = crearAleatorioInteger(66666);
    Arrays.sort((Object[])a1);
    Integer[] a2 = crearAleatorioInteger(66666);
    Arrays.sort((Object[])a2);
    Comparable[] c = (Comparable[])m.invoke((Object)null, new Object[] { a1, a2 });
    boolean res = true;
    for (int i = 1; i < c.length && res; i++)
      res = (c[i - 1].compareTo(c[i]) <= 0); 
    res = (res && c.length == a1.length + a2.length);
    if (res)
      return MAX_NOTA; 
    return 0.0D;
  }
  
  private static double testMergeSort2() {
    double MAX_NOTA = EJER_PUNTOS[3];
    int TALLA = 666666;
    Integer[] a1 = crearAleatorioInteger(666666);
    Integer[] a2 = Arrays.<Integer>copyOf(a1, a1.length);
    Arrays.sort((Object[])a1);
    Ordenacion.mergeSort2((Comparable[])a2);
    boolean res = sonIguales(a1, a2);
    if (res)
      return MAX_NOTA; 
    return 0.0D;
  }
  
  private static double testTermino() {
    double MAX_NOTA = EJER_PUNTOS[4];
    Termino saco1 = new Termino("saco", 1);
    Termino saco31 = new Termino("saco", 31);
    Termino saco4 = new Termino("saco", 4);
    boolean ok = (saco1.hashCode() == 422 && saco31.hashCode() == 3522362 && saco4.hashCode() == 9419);
    if (ok) {
      Termino asco1 = new Termino("asco", 1);
      Termino asco31 = new Termino("asco", 31);
      Termino asco4 = new Termino("asco", 4);
      ok = (asco1.hashCode() == 422 && asco31.hashCode() == 3003422 && asco4.hashCode() == 8555);
      if (ok) {
        boolean noIguales1 = saco1.equals(asco1);
        boolean noIguales31 = saco31.equals(asco31);
        boolean noIguales4 = saco4.equals(asco4);
        boolean iguales1 = saco1.equals(saco1);
        boolean iguales1Reves = asco1.equals(asco1);
        boolean iguales4 = saco4.equals(saco4);
        boolean iguales4Reves = asco4.equals(asco4);
        ok = (!noIguales1 && !noIguales31 && !noIguales4 && iguales1 && iguales1Reves && iguales4 && iguales4Reves);
      } 
    } 
    if (ok)
      return MAX_NOTA; 
    return 0.0D;
  }
  
  private static double testTablaHash() {
    double MAX_NOTA = EJER_PUNTOS[5];
    boolean ok = true;
    String res = "";
    TablaHash<Integer, Integer> t = new TablaHash<>(10);
    int t1 = 13;
    int nrH1 = 1 + (int)(t1 * 0.75D);
    for (int i = 0; i < nrH1 - 1; i++)
      t.insertar(new Integer(i), Integer.valueOf(0)); 
    t.insertar(new Integer(29), Integer.valueOf(0));
    if (t.talla() != nrH1) {
      ok = false;
      res = res + "\n\tTalla incorrecta";
    } else {
      int t2 = 29;
      int t2Ref = nuevaCapacidad(t1);
      if (t2 != t2Ref) {
        ok = false;
        res = res + "\n\tCapacidad del nuevo array INcorrecta ";
      } else {
        for (int j = 0; j < nrH1 - 1 && ok; j++) {
          Integer oi = t.recuperar(new Integer(j));
          if (oi == null) {
            ok = false;
            res = res + "\n\tNo se han copiado todos los datos";
          } 
        } 
        if (ok) {
          Integer oi = t.recuperar(new Integer(29));
          if (oi == null) {
            ok = false;
            res = res + "\n\tNo se han copiado todos los datos";
          } 
        } 
      } 
    } 
    if (ok)
      ok = testDesvTipica(); 
    if (ok)
      ok = testCosteMLocalizar(); 
    if (ok)
      return MAX_NOTA; 
    return 0.0D;
  }
  
  private static boolean testDesvTipica() {
    TablaHash<String, Integer> th = new TablaHash<>(40);
    for (int i = 0; i < 40; ) {
      th.insertar("N" + i, Integer.valueOf(i));
      i++;
    } 
    double tuDT = th.desviacionTipica();
    double miDT = 0.48381977964653394D;
    boolean res = (Math.abs(tuDT - miDT) < 1.0E-8D);
    return res;
  }
  
  private static boolean testCosteMLocalizar() {
    TablaHash<String, Integer> th = new TablaHash<>(40);
    for (int i = 0; i < 40; ) {
      th.insertar("N" + i, Integer.valueOf(i));
      i++;
    } 
    double tuCML = th.costeMLocalizar();
    double miCML = 1.0D;
    boolean res = (Math.abs(tuCML - miCML) < 1.0E-8D);
    if (res) {
      TablaHash<Integer, Integer> thI = new TablaHash<>(40);
      for (int j = 0; j < 10; j++)
        thI.insertar(new Integer(j * 53), Integer.valueOf(j)); 
      tuCML = thI.costeMLocalizar();
      miCML = 5.5D;
      res = (Math.abs(tuCML - miCML) < 1.0E-8D);
    } 
    return res;
  }
  
  private static double testRecEquilibrado() {
    double MAX_NOTA = EJER_PUNTOS[6];
    int TALLA = 511;
    int FACTOR1 = (int)(Math.random() * 511.0D);
    int FACTOR2 = (int)(Math.random() * 511.0D / 2.0D);
    Integer[] a = new Integer[511];
    for (int i = 0; i < a.length; i++)
      a[i] = Integer.valueOf(FACTOR2 + FACTOR1 * a.length - i); 
    ABB<Integer> aBB = generarABBPrueba(a);
    ABB<Integer> aEq = crearABBEquil(a);
    aBB.reconstruirEquilibrado();
    String porNiv1 = aBB.toStringPorNiveles();
    String porNiv2 = aEq.toStringPorNiveles();
    boolean res = porNiv1.equals(porNiv2);
    if (res)
      return MAX_NOTA; 
    return 0.0D;
  }
  
  private static double testEditorPredictivo() {
    double MAX_NOTA = EJER_PUNTOS[7];
    String DICCIONARIO = "aplicaciones" + File.separator + "editorPredictivo" + File.separator + "castellano.txt";
    int MAX_SUCESORES = 20;
    boolean ok = true;
    EditorPredictivo editor = new EditorPredictivo(DICCIONARIO);
    if (editor.talla() == 0)
      return 0.0D; 
    ListaConPI<String> lpi = editor.recuperarSucesores("estro", 19);
    String lpiSt = lpi.toString();
    String miLpiSt = "[estro, estrofa, estroncio, estropajo, estropajoso, estropeado, estropear, estropicio]";
    ok = miLpiSt.equals(lpiSt);
    if (ok)
      return MAX_NOTA; 
    return 0.0D;
  }
  
  private static Integer[] crearAleatorioInteger(int talla) {
    Integer[] aux = new Integer[talla];
    for (int i = 0; i < aux.length; i++)
      aux[i] = Integer.valueOf((int)(Math.random() * (10 * talla))); 
    return aux;
  }
  
  private static <T extends Comparable<T>> boolean sonIguales(T[] a, T[] b) {
    boolean iguales = true;
    if (a.length != b.length) {
      iguales = false;
    } else {
      for (int i = 0; i < a.length && iguales; i++)
        iguales = (a[i].compareTo(b[i]) == 0); 
    } 
    return iguales;
  }
  
  private static <C> boolean igualesDatos(ListaConPI<C> a, ListaConPI<C> b) {
    if (a == null || b == null)
      return false; 
    if (a.talla() != b.talla())
      return false; 
    a.inicio();
    while (!a.esFin()) {
      C sa = (C)a.recuperar();
      b.inicio();
      boolean esta = false;
      while (!b.esFin() && !esta) {
        if (sa.equals(b.recuperar())) {
          esta = true;
          continue;
        } 
        b.siguiente();
      } 
      if (b.esFin())
        return false; 
      a.siguiente();
    } 
    return true;
  }
  
  private static Method buscarMetodo(Class clase, String nombre) {
    Method m = null, methods[] = clase.getDeclaredMethods();
    for (int i = 0; i < methods.length && m == null; i++) {
      if (methods[i].getName().equalsIgnoreCase(nombre))
        m = methods[i]; 
    } 
    if (m != null)
      m.setAccessible(true); 
    return m;
  }
  
  private static ABB<Integer> generarABBPrueba(Integer[] a) {
    ABB<Integer> arbre = new ABB();
    for (int i = 0; i < a.length; ) {
      arbre.insertar(a[i]);
      i++;
    } 
    return arbre;
  }
  
  private static int nuevaCapacidad(int old) {
    return siguientePrimo(old * 2);
  }
  
  private static final int siguientePrimo(int n) {
    int nn = n;
    if (nn % 2 == 0)
      nn++; 
    for (; !esPrimo(nn); nn += 2);
    return nn;
  }
  
  private static final boolean esPrimo(int n) {
    for (int i = 3; i * i <= n; i += 2) {
      if (n % i == 0)
        return false; 
    } 
    return true;
  }
  
  private static ABB<Integer> crearABBEquil(Integer[] a) {
    Arrays.sort((Object[])a);
    ABB<Integer> aEq = new ABB();
    constEquil(aEq, a, 0, a.length - 1);
    return aEq;
  }
  
  private static void constEquil(ABB<Integer> aEq, Integer[] a, int ini, int fin) {
    if (ini <= fin) {
      int mitad = (ini + fin) / 2;
      aEq.insertar(a[mitad]);
      constEquil(aEq, a, ini, mitad - 1);
      constEquil(aEq, a, mitad + 1, fin);
    } 
  }
  
  private static void datosAlum() {
    alumno = System.getProperty("user.name");
    try {
      InetAddress localMachine = InetAddress.getLocalHost();
      pc = localMachine.getHostName();
    } catch (Exception e) {
      pc = "";
    } 
  }
  
  private static void salidaPantalla(Object x) {
    if (verb) {
      System.out.print(x);
    } else {
      System.out.print(".");
    } 
  }
}

