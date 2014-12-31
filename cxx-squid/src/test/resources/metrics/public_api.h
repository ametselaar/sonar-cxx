#define TEST_MACRO() \
	macro

/**
 testClass doc
 */
class testClass
{
	void defaultMethod();

public:
	// comment
	
	/** publicMethod doc */
	void publicMethod();
	
	/// classEnum doc
	enum classEnum {
		classEnumValue ///< classEnumValue doc
	}
	/// block enumVar doc
	enumVar; ///< inline enumVar doc, invalid ?
	 
	 /**
	 publicAttribute doc
	 */
	int publicAttribute;
	 
	int inlineCommentedAttr; //!< inlineCommentedAttr comment 

	void inlinePublicMethod(); //!< inlinePublicMethod comment
	
	int attr1, //!< attr1 doc 
	attr2; ///< attr2 doc
protected:
	/**
	 protectedMethod doc
	 */
	virtual void protectedMethod();
	 
	/**
	  protectedStruct doc
	*/
	struct protectedStruct {
	   int protectedStructField; ///< protectedStructField doc
	   int protectedStructField2; ///< protectedStructField2 doc  
	};
	 
	/*!
	  protectedClass doc
	*/
	class protectedClass {
	};
	 
	/**
	  operator doc
	*/
	value_t& operator[](std::size_t idx);
	 
private:
	void privateMethod();
	
	enum privateEnum {
		privateEnumVal
	};
	
	struct privateStruct {
		int privateStructField;
	};
	
	class privateClass {
		int i;
	};
	
	union privateUnion {
		int u;
	};
	
public:
	int inlineCommentedLastAttr; //!< inlineCommentedLastAttr comment
};

/// testStruct doc
struct testStruct {
	int testField; /**< inline testField comment */
	
	/// testTypeDef
	///
    typedef toto<T> testTypeDef;

    /**
     * bitfield doc
     */
    unsigned int bitfield:1;
};

/**
 globalVar doc
*/
extern int globalVar; 

int globalVarInline; //!< globalVarInline doc 

int
/**
globalVar1 doc
*/
globalVar1,
globalVar2, ///< globalVar2 doc
globalVar3; /*!< globalVar3 doc */

/// testFunction doc
void testFunction();

void testFunction2(); //!< testFunction2 doc

typedef int testType; ///< testType doc

/**
 testEnum doc
*/
enum testEnum
{
  /**
  enum_val doc
  */
  enum_val
};

enum testEnum enumVar1, ///< enumVar1 doc
/**
 enumVar2 doc
*/ 
enumVar2;

/*!
 testUnion doc
*/
union testUnion
{

};

int lastVar; ///< lastVar doc
