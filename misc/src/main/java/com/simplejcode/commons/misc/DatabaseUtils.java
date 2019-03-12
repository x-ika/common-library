package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.helpers.RDBRowModel;

import javax.naming.*;
import javax.sql.DataSource;
import java.beans.*;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.sql.*;
import java.sql.Statement;
import java.util.*;

public final class DatabaseUtils {

    private DatabaseUtils() {
    }

    //-----------------------------------------------------------------------------------
    /*
    Connection / Wrappers
     */

    public static DataSource getDataSource(String resourceName) throws NamingException {
        InitialContext initialContext = new InitialContext();
        return (DataSource) initialContext.lookup("java:comp/env/" + resourceName);
    }

    public static Connection getNativeConnection(String url, String user, String pass) throws ClassNotFoundException, SQLException {
        if (url.contains("oracle")) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } else if (url.contains("mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
        } else if (url.contains("postgresql")) {
            Class.forName("org.postgresql.Driver");
        }
        return DriverManager.getConnection(url, user, pass);
    }

    public static Connection getConnection(String resourceName) throws Exception {
        return getDataSource(resourceName).getConnection();
    }

    public static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (Exception ignore) {
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            statement.close();
        } catch (Exception ignore) {
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (Exception ignore) {
        }
    }


    public static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ignore) {
        }
    }

    public static void setAutoCommit(Connection connection, boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException ignore) {
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    Mostly Deprecated
     */

    @Deprecated
    public static String buildSelect(String table, String... cols) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(table).append(" where 1 = 1");
        for (String col : cols) {
            sql.append(" and ").append(col).append("=?");
        }
        return sql.toString();
    }

    @Deprecated
    public static String buildFuncSelect(String function, String table, String... cols) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(function).append(" from ").append(table).append(" where 1 = 1");
        for (String col : cols) {
            sql.append(" and ").append(col).append("=?");
        }
        return sql.toString();
    }

    @Deprecated
    public static String buildInsert(String table, String... cols) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(table).append(" (");
        StringBuilder values = new StringBuilder();
        for (String col : cols) {
            sql.append(col).append(',');
            values.append("?,");
        }
        sql.setCharAt(sql.length() - 1, ')');
        sql.append(" values(").append(values);
        sql.setCharAt(sql.length() - 1, ')');
        return sql.toString();
    }

    @Deprecated
    public static String buildUpdate(String table, int upd, String... cols) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(table).append(" set ");
        for (int i = 0; i < upd; i++) {
            sql.append(cols[i]).append("=?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where 1 = 1");
        for (int i = upd; i < cols.length; i++) {
            sql.append(" and ").append(cols[i]).append("=?");
        }
        return sql.toString();
    }

    @Deprecated
    public static String buildCall(int[][] outTypes, int nParams, String name) {
        StringBuilder sql = new StringBuilder("begin ");
        if (outTypes.length > 0 && outTypes[0][0] == 0) {
            outTypes[0][0]++;
            nParams--;
            sql.append("? := ");
        }
        sql.append(name).append('(');
        while (nParams-- > 0) {
            sql.append("?,");
        }
        if (sql.charAt(sql.length() - 1) == ',') {
            sql = sql.deleteCharAt(sql.length() - 1);
        }
        sql.append("); end;");
        return sql.toString();
    }

    @Deprecated
    public static String buildSPCall(String name, int nParams) {
        StringBuilder sql = new StringBuilder("call ").append(name).append('(');
        while (nParams-- > 0) {
            sql.append('?').append(',');
        }
        if (sql.charAt(sql.length() - 1) == ',') {
            sql = sql.deleteCharAt(sql.length() - 1);
        }
        return sql.append(')').toString();
    }

    //-----------------------------------------------------------------------------------
    /*
    Statement Wrappers
     */

    public static List<RDBRowModel> executeSelect(PreparedStatement statement) throws SQLException {
        List<RDBRowModel> ret = cursorToList(statement.executeQuery());
        statement.clearParameters();
        return ret;
    }

    public static long executeUpdate(PreparedStatement statement) throws SQLException {
        int updated = statement.executeUpdate();
        statement.clearParameters();
        return updated;
    }

    //-----------------------------------------------------------------------------------
    /*
    ORM Support
     */

    private static final Map<Class, Method[]> settersByClass = new HashMap<>();

    public static List<RDBRowModel> cursorToList(ResultSet rset) throws SQLException {
        List<RDBRowModel> list = new ArrayList<>();
        cursorToList(rset, list);
        return list;
    }

    public static void cursorToList(ResultSet rset, Collection<RDBRowModel> rows) throws SQLException {
        ResultSetMetaData data = rset.getMetaData();
        while (rset.next()) {
            RDBRowModel row = new RDBRowModel();
            for (int i = 1; i <= data.getColumnCount(); i++) {

                Object value = null;
                int type = data.getColumnType(i);
                switch (type) {
                    case Types.BIT:
                    case Types.TINYINT:
                    case Types.SMALLINT:
                    case Types.INTEGER:
                        value = rset.getInt(i);
                        break;
                    case Types.BIGINT:
                        value = rset.getLong(i);
                        break;
                    case Types.CHAR:
                    case Types.NCHAR:
                    case Types.VARCHAR:
                    case Types.NVARCHAR:
                    case Types.LONGVARCHAR:
                    case Types.LONGNVARCHAR:
                    case Types.CLOB:
                    case Types.NCLOB:
                    case Types.BINARY:
                    case Types.VARBINARY:
                    case Types.LONGVARBINARY:
                        value = rset.getString(i);
                        break;
                    case Types.FLOAT:
                    case Types.REAL:
                    case Types.DOUBLE:
                    case Types.DECIMAL:
                    case Types.NUMERIC:
                        value = rset.getDouble(i);
                        break;
                    case Types.DATE:
                        value = rset.getDate(i);
                        break;
                    case Types.TIME:
                        value = rset.getTime(i);
                        break;
                    case Types.TIMESTAMP:
                        value = rset.getTimestamp(i);
                        break;
                    case Types.REF:
                        List<RDBRowModel> list = new ArrayList<>();
                        value = list;
                        cursorToList((ResultSet) rset.getObject(i), list);
                        break;
                    default:
                        System.out.println("Unknown SQL type detected " + type);
                }

                row.put(data.getColumnName(i), value);

            }
            rows.add(row);
        }
        closeResultSet(rset);
    }

    public static <T> List<T> convertToBeans(Collection<RDBRowModel> rows, Class<T> clazz) {

        List<T> list = new ArrayList<>(rows.size());
        for (RDBRowModel row : rows) {
            list.add(convertToBean(row, clazz));
        }
        return list;

    }

    public static <T> T convertToBean(RDBRowModel row, Class<T> clazz) {
        try {

            T entity = clazz.getConstructor().newInstance();

            Method[] propertyDescriptors = getSetterMethods(clazz);
            for (Method setter : propertyDescriptors) {

                String fieldName = toDatabaseFieldName(setter.getName());
                Object fieldValue = row.get(fieldName);

                setter.invoke(entity, fieldValue);

            }

            return entity;

        } catch (Exception e) {
            throw convert(e);
        }
    }


    private static <T> Method[] getSetterMethods(Class<T> clazz) {
        return settersByClass.computeIfAbsent(clazz, DatabaseUtils::getSetterMethods0);
    }

    private static Method[] getSetterMethods0(Class<?> clazz) {

        try {
            List<Method> setters = new ArrayList<>(16);

            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor desc : beanInfo.getPropertyDescriptors()) {
                Method setter = desc.getWriteMethod();
                if (setter != null) {
                    setters.add(setter);
                }
            }
            return setters.toArray(new Method[setters.size()]);
        } catch (Exception e) {
            throw convert(e);
        }

    }

    private static String toDatabaseFieldName(String name) {
        char[] chars = new char[name.length() - 3 << 1];
        int length = 0;
        for (int i = 0; i < name.length() - 3; i++) {
            char ch = name.charAt(i + 3);
            if ('A' <= ch && ch <= 'Z') {
                if (i != 0) {
                    chars[length++] = '_';
                }
                chars[length++] = (char) (ch - 'A' + 'a');
            } else {
                chars[length++] = ch;
            }
        }
        return new String(chars, 0, length);
    }

    //-----------------------------------------------------------------------------------
    /*
    Stored Procedure Execution
     */

    public enum MethodType {
        FUNCTION,
        PROCEDURE
    }

    public static final Object DEFAULT = new Object();

    private static Map<Class, String> mappedTypes = new Hashtable<>();

    //-----------------------------------------------------------------------------------

    public static Object[] execute(Connection c, MethodType methodType, String methodName,
                                   long in, long inout, int[] outTypes, int[] inNullTypes, Object... inputParams)
    {
        final int nParams = inputParams.length + outTypes.length - Long.bitCount(inout);
        long all = (1L << nParams + 1) - 2;
        long out = all ^ in ^ inout;
        if (nParams > 63) {
            throw bug("execute method works only for the functions having less than 64 parameters");
        }
        if (methodType == null) {
            throw bug("Unknown database method type " + methodType);
        }
        if (Long.bitCount(in) != inputParams.length) {
            throw bug("Invalid arguments specified (in,inputParams)" + Long.bitCount(in) + " " + inputParams.length);
        }
        if (Long.bitCount(out) != outTypes.length) {
            throw bug("Invalid arguments specified (out,outTypes)" + Long.bitCount(out) + " " + outTypes.length);
        }

        long def = 0;
        for (int i = 1, j = 0; i <= nParams; i++) {
            if ((in & 1L << i) != 0 && inputParams[j++] == DEFAULT) {
                def |= 1L << i;
            }
        }

        StringBuilder sql = new StringBuilder(1 << 8);
        sql.append("begin ");
        if (methodType == MethodType.FUNCTION) {
            sql.append("? := ");
        }
        sql.append(methodName).append('(');
        for (int i = methodType == MethodType.FUNCTION ? 2 : 1; i <= nParams; i++) {
            sql.append((def & 1L << i) == 0 ? "?," : " ,");
        }
        if (sql.charAt(sql.length() - 1) == ',') {
            sql = sql.deleteCharAt(sql.length() - 1);
        }
        sql.append("); end;");

        CallableStatement statement = null;
        try {

            if (Arrays.stream(inputParams).anyMatch(p -> p != null && mappedTypes.containsKey(p.getClass()))) {
                c = c.getMetaData().getConnection();
            }

            statement = c.prepareCall(sql.toString());

            for (int i = 1, j = 0, k = 0, t = 0; i <= nParams; i++) {
                if ((def & 1L << i) != 0) {
                    t++;
                }
                int ind = i - t;
                if ((out & 1L << i) != 0) {
                    statement.registerOutParameter(ind, outTypes[j++]);
                }
                if ((in & 1L << i) != 0) {
                    Object param = inputParams[k++];

                    if (param == DEFAULT) {
                        continue;
                    }
                    if (param == null && inNullTypes != null) {
                        statement.setNull(ind, inNullTypes[i]);
                    } else if (param instanceof byte[]) {
                        final byte[] bytes = (byte[]) param;
                        statement.setBinaryStream(ind, new ByteArrayInputStream(bytes), bytes.length);
                    } else {
                        statement.setObject(ind, param);
                    }
                }
            }

            statement.execute();
            Object[] ret = new Object[outTypes.length];
            for (int i = 1, j = 0; i <= nParams; i++) {
                if ((out & 1L << i) != 0) {
                    Object o = statement.getObject(i);
                    if (o instanceof Blob) {
                        Blob blob = (Blob) o;
                        ret[j++] = blob.getBytes(1, (int) blob.length());
                        continue;
                    }
                    ret[j++] = o;
                }
            }
            return ret;

        } catch (SQLException e) {
            throw databaseMethodCallFailed(methodName, e);
        } finally {
            closeStatement(statement);
        }
    }

    //-----------------------------------------------------------------------------------

    /**
     * @param methodType  METHOD_TYPE_FUNCTION or MethodType.PROCEDURE
     * @param methodName  name of the db procedure or function
     * @param in          set of indexes of the in parameters
     * @param inout       set of indexes of the inout parameters. Please note that outTypes array should also contain these indices
     * @param outTypes    array of int[2] where each element represents one output parameter:
     *                    first int contains the index of the output parameter and second int - sql type of that parameter
     * @param inputParams array contains the objects which should be passed to db procedure or function
     * @param inNullTypes array contains types of objects that may be null
     * @return array of objects returned
     */
    public static Object[] callMethod(Connection c, MethodType methodType, String methodName,
                                      long in, long inout, int[] outTypes, int[] inNullTypes, Object... inputParams)
    {
        return execute(c, methodType, methodName, in, inout, outTypes, inNullTypes, inputParams);
    }

    public static Object[] callNormalMethod(Connection c, MethodType methodType, String methodName, int[] outTypes, Object... inputParams) {
        return callMethod(c, methodType, methodName, getInputSet(methodType, inputParams.length), 0, outTypes, null, inputParams);
    }

    public static Object[][] batchCallMethod(Connection c, MethodType methodType, String methodName,
                                             int[] outTypes, Object[][] parametersSet)
    {
        List<Object[]> outParams = new ArrayList<>();
        try {
            c.setAutoCommit(false);
            for (Object[] p : parametersSet) {
                Object[] result = execute(c, methodType, methodName, getInputSet(methodType, p.length), 0, outTypes, null, p);
                outParams.add(result);
            }
            c.commit();
        } catch (Exception e) {
            rollback(c);
        } finally {
            setAutoCommit(c, true);
        }
        return outParams.toArray(new Object[outParams.size()][]);
    }

    private static long getInputSet(MethodType methodType, int nInput) {
        return (1L << nInput) - 1 << (methodType == MethodType.PROCEDURE ? 1 : 2);
    }

    //-----------------------------------------------------------------------------------

    public static Object[] callProcedure(Connection c, String methodName, int[] outTypes, Object... inputParams) {
        return callNormalMethod(c, MethodType.PROCEDURE, methodName, outTypes, inputParams);
    }

    public static Object[] callProcedure(Connection c, String methodName, int resultCodeIndex, int[] outTypes, Object... inputParams) {
        Object[] outParams = callNormalMethod(c, MethodType.PROCEDURE, methodName, outTypes, inputParams);
        checkResultCode(methodName, resultCodeIndex, outParams);
        return outParams;
    }

    public static Object[] callFunction(Connection c, String methodName, int[] outTypes, Object... inputParams) {
        return callNormalMethod(c, MethodType.FUNCTION, methodName, outTypes, inputParams);
    }

    public static Object[] callFunction(Connection c, String methodName, int resultCodeIndex, int[] outTypes, Object... inputParams) {
        Object[] outParams = callNormalMethod(c, MethodType.FUNCTION, methodName, outTypes, inputParams);
        checkResultCode(methodName, resultCodeIndex, outParams);
        return outParams;
    }

    public static Object[][] batchCallProcedure(Connection c, String methodName, int[] outTypes, Object[][] parametersSet) {
        return batchCallMethod(c, MethodType.PROCEDURE, methodName, outTypes, parametersSet);
    }

    public static Object[][] batchCallProcedure(Connection c, String methodName, int resultCodeIndex, int[] outTypes, Object[][] parametersSet) {
        Object[][] outParams = batchCallMethod(c, MethodType.PROCEDURE, methodName, outTypes, parametersSet);
        for (Object[] outParam : outParams) {
            checkResultCode(methodName, resultCodeIndex, outParam);
        }
        return outParams;
    }

    public static void checkResultCode(String methodName, int resultCodeIndex, Object[] outParams) {
        Integer code = (Integer) outParams[resultCodeIndex];
        if (code != null && code != 0) {
            databaseMethodCallError(methodName.toUpperCase(), String.valueOf(code));
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    TO DO
     */

    private static RuntimeException bug(String s) {
        return new RuntimeException(s);
    }

    private static RuntimeException databaseMethodCallFailed(String methodName, SQLException e) {
        return new RuntimeException(methodName, e);
    }

    private static void databaseMethodCallError(String s, String code) {
    }


    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
