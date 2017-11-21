package CRM;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
//import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

import java.io.*;

/**
 * CRM OWL Ontology
 * based on some Examples of E. Ammann *
 * by Marcus Danzer & Marcel Sailer @ reutlingen-university
 */

public class CRM {

    private static final String BASE_URL = "http://www.semanticweb.org/max/ontologies/2017/10/untitled-ontology-5";
    private static final String FILE_LOCATION = "CRM_Ontology.owl";
    private static File output = new File("CRM_Ontology_new.owl");

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

/*
        //create individual for the malePerson class
        OWLIndividual norbert = factory.getOWLNamedIndividual(":Norbert", pm);
        //OWLDeclarationAxiom declarationAxiom4 = factory.getOWLDeclarationAxiom(norbert);
        //manager.addAxiom(ontology, declarationAxiom4);
        OWLClassAssertionAxiom caAxiom = factory.getOWLClassAssertionAxiom(malePerson, norbert);
        manager.addAxiom(ontology, caAxiom);

*/
        // Kunde wird erzeugt
        OWLIndividual peter = factory.getOWLNamedIndividual(":Peter", pm);
        OWLClass kundenClass = factory.getOWLClass(":Kunde", pm);
        OWLClassAssertionAxiom caAxiom = factory.getOWLClassAssertionAxiom(kundenClass, peter);
        manager.addAxiom(ontology, caAxiom);

        //Mitarbeiter wird erzeugt
        OWLIndividual georg = factory.getOWLNamedIndividual(":Georg", pm);
        OWLClass mitarbeiterClass = factory.getOWLClass(":Mitarbeiter", pm);
        OWLClassAssertionAxiom caAxiom2 = factory.getOWLClassAssertionAxiom(mitarbeiterClass, georg);
        manager.addAxiom(ontology, caAxiom2);

        //Property Assertion Georg betreut Peter
        OWLObjectProperty betreut = factory.getOWLObjectProperty(":betreut", pm);
        OWLObjectPropertyAssertionAxiom pAAxiom1 = factory.getOWLObjectPropertyAssertionAxiom(betreut, georg, peter);
        manager.addAxiom(ontology, pAAxiom1);

        //


/*
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
*/
        IRI documentIRI2 = IRI.create(output);
        manager.saveOntology(ontology, documentIRI2);
        System.out.println("\nExtended Ontology stored in " + documentIRI2 + "\n");

    }
}