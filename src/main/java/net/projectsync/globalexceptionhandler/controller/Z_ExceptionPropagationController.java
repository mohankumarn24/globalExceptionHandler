package net.projectsync.globalexceptionhandler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/books")
public class Z_ExceptionPropagationController {

	// ========================================================
    // 1. RuntimeException Cases
	// ========================================================

    /**
     * RuntimeException propagates out of method.
     * RestControllerAdvice can handle it.
     */
    @GetMapping("/runtime1")
    public String runtime1() {
        throw new RuntimeException("RuntimeException propagated");
    }

    /**
     * RuntimeException caught and consumed locally.
     * RestControllerAdvice is NOT invoked.
     */
    @GetMapping("/runtime2")
    public String runtime2() {
        try {
            throw new RuntimeException("RuntimeException");
        } catch (RuntimeException e) {
            return "Handled locally";
        }
    }

    /**
     * RuntimeException caught and rethrown.
     * RestControllerAdvice can handle it.
     */
    @GetMapping("/runtime3")
    public String runtime3() {
        try {
            throw new RuntimeException("RuntimeException");
        } catch (RuntimeException e) {
            throw e;
        }
    }

    // ========================================================
    // 2. Checked Exception (SQLException) Cases
    //    Same exception propagation rules as RuntimeException
    // ========================================================

    /**
     * Checked exception propagates out of method.
     * RestControllerAdvice can handle it.
     */
    @GetMapping("/sql1")
    public String sql1() throws SQLException {
        throw new SQLException("SQLException propagated");
    }

    /**
     * Checked exception caught and consumed locally.
     * RestControllerAdvice is NOT invoked.
     */
    @GetMapping("/sql2")
    public String sql2() {
        try {
            throw new SQLException("SQLException");
        } catch (SQLException e) {
            return "Handled locally";
        }
    }

    /**
     * Checked exception caught and rethrown.
     * RestControllerAdvice can handle it.
     */
    @GetMapping("/sql3")
    public String sql3() throws SQLException {
        try {
            throw new SQLException("SQLException");
        } catch (SQLException e) {
            throw e;
        }
    }
    
	// ========================================================
	// 3. Wrapped Exception Case
	// 	  SQLException is wrapped inside RuntimeException.
	//    Spring chooses the handler for RuntimeException,
	//    because that is what propagates out of the method.
	// ========================================================

	/**
	 * Checked exception caught and wrapped inside RuntimeException.
	 * Spring sees RuntimeException (not SQLException).
	 * Therefore @ExceptionHandler(RuntimeException.class) is invoked.
	 */
	@GetMapping("/sql4")
	public String sql4() {
		try {
			throw new SQLException("DB Error");
		} catch (SQLException e) {
			// Spring sees RuntimeException as the exception that propagates out of the controller.
			// The SQLException is only the root cause.
			throw new RuntimeException(e);
		}
	}
}

/*
RuntimeException 		-> @ExceptionHandler(RuntimeException.class)
NullPointerException 	-> @ExceptionHandler(RuntimeException.class)
SQLException     		-> @ExceptionHandler(Exception.class)
IOException      		-> @ExceptionHandler(Exception.class)
*/

/*
Throwable
├── Error
└── Exception
    ├── RuntimeException
    │   ├── NullPointerException
    │   ├── IllegalArgumentException
    │   └── ...
    ├── SQLException
    ├── IOException
    └── ...

Global Exception Handlers:

    @ExceptionHandler(NullPointerException.class)
    public void handleNPE(...) {}

    @ExceptionHandler(RuntimeException.class)
    public void handleRTE(...) {}

    @ExceptionHandler(Exception.class)
    public void handleException(...) {}

Spring chooses the MOST SPECIFIC matching handler.
Examples:

	throw new NullPointerException();
	    -> handleNPE()
	
	throw new IllegalArgumentException();
	    -> handleRTE()
	
	throw new RuntimeException();
	    -> handleRTE()
	
	throw new SQLException();
	    -> handleException()
	
	throw new IOException();
	    -> handleException()
	    
    throw new RuntimeException(new SQLException("DB Error"));
        -> handleRTE()	    
        -> Reason: 
        	-- Spring sees RuntimeException as the exception that propagates out of the controller. 
        	-- The SQLException is only the root cause.
*/
