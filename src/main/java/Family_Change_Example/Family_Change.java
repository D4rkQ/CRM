package Family_Change_Example;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
//import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

import java.io.*;

/**
 * Example add new entities to an OWL ontology.
 * (E. Ammann 2014)
 */

public class Family_Change {

    private static final String BASE_URL = "http://www.reutlingen-university.de/ontologies/family2.owl";
    private static final String FILE_LOCATION = "family2.owl";
    private static File output = new File("family2_new.owl");

    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException
    {

        //prepare ontology
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File(FILE_LOCATION);
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        OWLDataFactory factory = manager.getOWLDataFactory();
        PrefixOWLOntologyFormat pm = (PrefixOWLOntologyFormat) manager.getOntologyFormat(ontology);
        pm.setDefaultPrefix(BASE_URL + "#");

	IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
        System.out.println("\nOntology loaded from: " + documentIRI + "\n");

	//add new classes Person, MalePerson and FemalePerson to the ontology
	OWLClass person = factory.getOWLClass(":Person", pm);
	OWLClass malePerson = factory.getOWLClass(":MalePerson", pm);
	OWLClass femalePerson = factory.getOWLClass(":FemalePerson", pm);

        OWLDeclarationAxiom declarationAxiom1 = factory.getOWLDeclarationAxiom(person);
        manager.addAxiom(ontology, declarationAxiom1);
        OWLDeclarationAxiom declarationAxiom2 = factory.getOWLDeclarationAxiom(malePerson);
        manager.addAxiom(ontology, declarationAxiom2);
        OWLDeclarationAxiom declarationAxiom3 = factory.getOWLDeclarationAxiom(femalePerson);
        manager.addAxiom(ontology, declarationAxiom3);

	//add subclass relations to the new classes
	OWLAxiom axiom1 = factory.getOWLSubClassOfAxiom(malePerson, person);
	OWLAxiom axiom2 = factory.getOWLSubClassOfAxiom(femalePerson, person);
	AddAxiom addAxiom1 = new AddAxiom(ontology, axiom1); 
	AddAxiom addAxiom2 = new AddAxiom(ontology, axiom2);
	manager.applyChange(addAxiom1);
	manager.applyChange(addAxiom2);

	//create individual for the malePerson class
	OWLIndividual norbert = factory.getOWLNamedIndividual(":Norbert", pm);
	//OWLDeclarationAxiom declarationAxiom4 = factory.getOWLDeclarationAxiom(norbert);
	//manager.addAxiom(ontology, declarationAxiom4);

	OWLClassAssertionAxiom caAxiom = factory.getOWLClassAssertionAxiom(malePerson, norbert);
	manager.addAxiom(ontology, caAxiom);  

	// Individual Norbert also should belong to class Father
	OWLClass father = factory.getOWLClass(":Father", pm);	
	OWLClassAssertionAxiom caAxiom2 = factory.getOWLClassAssertionAxiom(father, norbert);
	manager.addAxiom(ontology, caAxiom2);  

	//create a property
	OWLObjectProperty isA = factory.getOWLObjectProperty(":isA", pm);
	OWLDeclarationAxiom declarationAxiom5 = factory.getOWLDeclarationAxiom(isA);
	manager.addAxiom(ontology, declarationAxiom5);	

	//define domain and range for the property isA
	OWLObjectPropertyDomainAxiom opd1 = factory.getOWLObjectPropertyDomainAxiom(isA, father);
	OWLObjectPropertyRangeAxiom opd2 = factory.getOWLObjectPropertyRangeAxiom(isA, malePerson);
	manager.addAxiom(ontology, opd1);
	manager.addAxiom(ontology, opd2); 

	IRI documentIRI2 = IRI.create(output);
	manager.saveOntology(ontology, documentIRI2);
        System.out.println("\nExtended Ontology stored in " + documentIRI2 + "\n");

   }
}