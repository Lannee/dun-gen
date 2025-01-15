import React from 'react';
import { Card } from 'primereact/card';
import { TabView, TabPanel } from 'primereact/tabview';
import { ListBox } from 'primereact/listbox';

const CharacterSheet = ({ character }) => {
    const {
        name,
        level,
        experience,
        class_name,
        race,
        background,
        alignment,
        ability_scores,
        skills,
        equipment,
        features_and_traits,
        personality,
        goals
    } = character;

    return (
        <div className="character-sheet">
            <Card title={`Character Sheet: ${name || 'Unnamed Character'}`} style={{ width: '30rem' }}>
                <TabView>
                    <TabPanel header="Overview">
                        <p><strong>Level:</strong> {level}</p>
                        <p><strong>Experience:</strong> {experience}</p>
                        <p><strong>Class:</strong> {class_name}</p>
                        <p><strong>Race:</strong> {race}</p>
                        <p><strong>Background:</strong> {background}</p>
                        <p><strong>Alignment:</strong> {alignment}</p>
                    </TabPanel>
                    <TabPanel header="Ability Scores">
                        <ul>
                            {Object.entries(ability_scores).map(([key, value]) => (
                                <li key={key}><strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}</li>
                            ))}
                        </ul>
                    </TabPanel>
                    <TabPanel header="Skills">
                        <ul>
                            {Object.entries(skills).map(([key, value]) => (
                                <li key={key}><strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}</li>
                            ))}
                        </ul>
                    </TabPanel>
                    <TabPanel header="Equipment">
                        <ListBox options={equipment.map(item => ({ label: item.name || 'Unnamed', value: item.description }))} />
                    </TabPanel>
                    <TabPanel header="Features & Traits">
                        <ul>
                            {features_and_traits.map((feature, index) => (
                                <li key={index}>
                                    <strong>{feature.name || 'Unnamed Feature'}:</strong> {feature.description}
                                </li>
                            ))}
                        </ul>
                    </TabPanel>
                    <TabPanel header="Personality">
                        <p><strong>Traits:</strong></p>
                        <ul>
                            {personality.traits.map((trait, index) => (
                                <li key={index}>{trait}</li>
                            ))}
                        </ul>
                        <p><strong>Backstory:</strong> {personality.backstory}</p>
                    </TabPanel>
                    <TabPanel header="Goals">
                        <ul>
                            {goals.map((goal, index) => (
                                <li key={index}>{goal}</li>
                            ))}
                        </ul>
                    </TabPanel>
                </TabView>
            </Card>
        </div>
    );
};

export default CharacterSheet;
